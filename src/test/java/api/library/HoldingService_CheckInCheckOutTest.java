package api.library;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static util.matchers.HasExactlyItemsInAnyOrder.hasExactlyItemsInAnyOrder;
import java.util.Date;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;
import util.DateUtil;

// TODO stub to avoid persistence (even despite being in memory)

public class HoldingService_CheckInCheckOutTest {
   private HoldingService service = new HoldingService();
   private PatronService patronService = new PatronService();
   private ClassificationApi classificationApi = mock(ClassificationApi.class);
   private String patronId;
   private String branchScanCode;
   private String bookHoldingBarcode;

   @Before
   public void initialize() {
      LibraryData.deleteAll();
      ClassificationApiFactory.setService(classificationApi);
      branchScanCode = new BranchService().add("a branch name");
      patronId = patronService.add("joe");
      bookHoldingBarcode = addBookHolding();
   }

   private String addBookHolding() {
      MaterialDetails material = new MaterialDetails("123", "", "", "", MaterialType.Book, "");
      when(classificationApi.getMaterialDetails("123")).thenReturn(material);
      return service.add("123", branchScanCode);
   }

   @Test
   public void holdingMadeUnavailableOnCheckout() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());

      assertThat(service.isAvailable(bookHoldingBarcode), equalTo(false));
   }

   @Test(expected = HoldingNotFoundException.class)
   public void checkoutThrowsWhenHoldingIdNotFound() {
      service.checkOut(patronId, "999:1", new Date());
   }

   @Test(expected = HoldingAlreadyCheckedOutException.class)
   public void checkoutThrowsWhenUnavailable() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());

      service.checkOut(patronId, bookHoldingBarcode, new Date());
   }

   @Test
   public void updatesPatronWithHoldingOnCheckout() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());

      HoldingMap patronHoldings = patronService.find(patronId).holdings();
      assertThat(patronHoldings.holdings(), hasExactlyItemsInAnyOrder(service.find(bookHoldingBarcode)));
   }

   @Test
   public void returnsHoldingToBranchOnCheckIn() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());

      service.checkIn(bookHoldingBarcode, DateUtil.tomorrow(), branchScanCode);

      Holding holding = service.find(bookHoldingBarcode);
      assertTrue(holding.isAvailable());
      assertThat(holding.getBranch().getScanCode(), equalTo(branchScanCode));
   }

   @Test
   public void removesHoldingFromPatronOnCheckIn() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());

      service.checkIn(bookHoldingBarcode, DateUtil.tomorrow(), branchScanCode);

      assertTrue(patronService.find(patronId).holdings().isEmpty());
   }

   @Test
   public void answersDueDate() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());

      Date due = service.dateDue(bookHoldingBarcode);

      Holding holding = service.find(bookHoldingBarcode);
      assertThat(due, equalTo(holding.dateDue()));
   }

   @Test
   public void checkinReturnsDaysLate() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());
      Date fiveDaysLate = DateUtil.addDays(service.dateDue(bookHoldingBarcode), 5);

      int daysLate = service.checkIn(bookHoldingBarcode, fiveDaysLate, branchScanCode);

      assertThat(daysLate, equalTo(5));
   }

   @Test
   public void updatesFinesOnLateCheckIn() {
      service.checkOut(patronId, bookHoldingBarcode, new Date());
      Holding holding = service.find(bookHoldingBarcode);
      Date oneDayLate = DateUtil.addDays(holding.dateDue(), 1);

      service.checkIn(bookHoldingBarcode, oneDayLate, branchScanCode);

      assertThat(patronService.find(patronId).fineBalance(), equalTo(MaterialType.Book.getDailyFine()));
   }
}
