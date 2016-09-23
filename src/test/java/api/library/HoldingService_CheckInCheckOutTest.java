package api.library;

import static domain.core.BranchTest.EAST_SCAN;
import static domain.core.MaterialTestData.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Date;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;
import util.DateUtil;

public class HoldingService_CheckInCheckOutTest {
   private HoldingService service = new HoldingService();
   private PatronService patronService = new PatronService();
   private ClassificationApi classificationApi = mock(ClassificationApi.class);
   private String patronId;
   private String branchScanCode;

   @Before
   public void initialize() {
      LibraryData.deleteAll();
      ClassificationApiFactory.setService(classificationApi);
      branchScanCode = new BranchService().add("a branch name");
      patronId = patronService.add("joe");
   }

   private String addHolding(MaterialDetails material) {
      when(classificationApi.getMaterialDetails(material.getSourceId())).thenReturn(material);
      return service.add(material.getSourceId(), branchScanCode);
   }

   @Test
   public void holdingMadeUnavailableOnCheckout() {
      String barcode = addHolding(THE_TRIAL);
      service.checkOut(patronId, barcode, new Date());

      assertThat(service.isAvailable(barcode), is(false));
   }

   @Test(expected = HoldingNotFoundException.class)
   public void checkoutThrowsWhenHoldingIdNotFound() {
      service.checkOut(patronId, "999:1", new Date());
   }

   @Test(expected = HoldingAlreadyCheckedOutException.class)
   public void checkoutThrowsWhenUnavailable() {
      String barcode = addHolding(THE_TRIAL);
      service.checkOut(patronId, barcode, new Date());

      service.checkOut(patronId, barcode, new Date());
   }

   @Test
   public void updatesPatronWithHoldingOnCheckout() {
      String barcode = addHolding(THE_TRIAL);

      service.checkOut(patronId, barcode, new Date());

      HoldingMap patronHoldings = patronService.find(patronId).holdings();
      assertEquals(1, patronHoldings.size());
      assertTrue(patronHoldings.contains(service.find(barcode)));
   }

   @Test
   public void returnsHoldingToBranchOnCheckIn() {
      String barCode = addHolding(AGILE_JAVA);
      service.checkOut(patronId, barCode, new Date());

      service.checkIn(barCode, DateUtil.tomorrow(), branchScanCode);

      Holding holding = service.find(barCode);
      assertTrue(holding.isAvailable());
      assertEquals(branchScanCode, holding.getBranch().getScanCode());
   }

   @Test
   public void removesHoldingFromPatronOnCheckIn() {
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(patronId, barCode, new Date());

      service.checkIn(barCode, DateUtil.tomorrow(), branchScanCode);

      assertTrue(patronService.find(patronId).holdings().isEmpty());
   }

   // TODO stub to avoid persistence (even despite being in memory)

   @Test
   public void answersDueDate() {
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(patronId, barCode, new Date());

      Date due = service.dateDue(barCode);

      Holding holding = service.find(barCode);
      assertThat(due, is(holding.dateDue()));
   }

   @Test
   public void checkinReturnsDaysLate() {
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(patronId, barCode, new Date());
      Date fiveDaysLate = DateUtil.addDays(service.dateDue(barCode), 5);

      int daysLate = service.checkIn(barCode, fiveDaysLate, EAST_SCAN);

      assertThat(daysLate, is(5));
   }

   @Test
   public void updatesFinesOnLateCheckIn() {
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(patronId, barCode, new Date());
      Holding holding = service.find(barCode);
      Date oneDayLate = DateUtil.addDays(holding.dateDue(), 1);

      service.checkIn(barCode, oneDayLate, branchScanCode);

      assertEquals(MaterialType.Book.getDailyFine(), patronService.find(patronId).fineBalance());
   }
}
