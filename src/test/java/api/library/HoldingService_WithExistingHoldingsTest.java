package api.library;

import static domain.core.BranchTest.*;
import static domain.core.MaterialTestData.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Date;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;
import persistence.PatronStore;
import util.DateUtil;

public class HoldingService_WithExistingHoldingsTest {
   private HoldingService service;
   private String joeId;
   private ClassificationApi classificationApi;
   private String branchScanCode;

   @Before
   public void initialize() {
      LibraryData.deleteAll();
      classificationApi = mock(ClassificationApi.class);
      ClassificationApiFactory.setService(classificationApi);
      service = new HoldingService();

      branchScanCode = add("a branch name");
   }

   private void addPatronJoe() {
      joeId = new PatronService().add("joe");
   }

   private String add(String name) {
      return new BranchService().add(name);
   }

   private Holding addHolding(String branchScanCode, MaterialDetails material) {
      when(classificationApi.getMaterialDetails(material.getSourceId())).thenReturn(material);
      String barcode = service.add(material.getSourceId(), branchScanCode);
      return service.find(barcode);
   }

   private String addHolding(MaterialDetails material) {
      return addHolding(branchScanCode, material).getBarCode();
   }

   @Test
   public void returnsEntireInventoryOfHoldings() {
      // TODO
      addHolding(THE_TRIAL);
      addHolding(THE_TRIAL);
      addHolding(THE_TRIAL);
      HoldingMap holdings = service.allHoldings();

      assertEquals(3, holdings.size());
//      assertThat(holdings,
//         hasItems(theTrialCopy1AtEast, theTrialCopy2AtWest, agileJavaAtWest));
   }

   @Test
   public void storesNewHoldingAtBranch() {
      String barcode = addHolding(AGILE_JAVA);

      assertEquals(branchScanCode, service.find(barcode).getBranch().getScanCode());
   }

   @Test
   public void findByBarCodeReturnsNullWhenNotFound() {
      assertThat(service.find("999:1"), is((Holding)null));
   }

   @Test
   public void updatesBranchOnHoldingTransfer() {
      String barcode = addHolding(AGILE_JAVA);
      Holding holding = service.find(barcode);

      // TODO change to take a barcode
      service.transfer(holding, BRANCH_EAST);

      holding = service.find(barcode);
      assertEquals(BRANCH_EAST, holding.getBranch());
   }

   @Test
   public void holdingIsAvailableWhenNotCheckedOut() {
      String barcode = addHolding(AGILE_JAVA);

      assertThat(service.isAvailable(barcode), is(true));
   }

   @Test
   public void holdingMadeUnavailableOnCheckout() {
      addPatronJoe();
      String barcode = addHolding(THE_TRIAL);
      service.checkOut(joeId, barcode, new Date());

      assertThat(service.isAvailable(barcode), is(false));
   }

   @Test(expected = HoldingNotFoundException.class)
   public void availabilityCheckThrowsWhenHoldingNotFound() {
      service.isAvailable("345:1");
   }

   @Test(expected = HoldingNotFoundException.class)
   public void checkoutThrowsWhenHoldingIdNotFound() {
      service.checkOut(joeId, "999:1", new Date());
   }

   @Test(expected = HoldingNotFoundException.class)
   public void checkinThrowsWhenHoldingIdNotFound() {
      service.checkIn("999:1", new Date(), branchScanCode);
   }

   @Test(expected = HoldingAlreadyCheckedOutException.class)
   public void checkoutThrowsWhenUnavailable() {
      addPatronJoe();
      String barcode = addHolding(THE_TRIAL);
      service.checkOut(joeId, barcode, new Date());

      service.checkOut(joeId, barcode, new Date());
   }

   @Test
   public void updatesPatronWithHoldingOnCheckout() {
      addPatronJoe();
      String barcode = addHolding(THE_TRIAL);

      service.checkOut(joeId, barcode, new Date());

      HoldingMap patronHoldings = retrieve(joeId).holdings();
      assertEquals(1, patronHoldings.size());
      assertTrue(patronHoldings.contains(service.find(barcode)));
   }

   @Test
   public void returnsHoldingToBranchOnCheckIn() {
      addPatronJoe();
      String barCode = addHolding(AGILE_JAVA);
      service.checkOut(joeId, barCode, new Date());
      service.checkIn(barCode, DateUtil.tomorrow(), branchScanCode);

      Holding holding = service.find(barCode);
      assertTrue(holding.isAvailable());
      assertEquals(branchScanCode, holding.getBranch().getScanCode());
   }

   @Test
   public void removesHoldingFromPatronOnCheckIn() {
      addPatronJoe();
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(joeId, barCode, new Date());

      service.checkIn(barCode, DateUtil.tomorrow(), branchScanCode);

      assertTrue(retrieve(joeId).holdings().isEmpty());
   }

   // TODO stub to avoid persistence (even despite being in memory)

   @Test
   public void answersDueDate() {
      addPatronJoe();
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(joeId, barCode, new Date());

      Date due = service.dateDue(barCode);

      Holding holding = service.find(barCode);
      assertThat(due, is(holding.dateDue()));
   }

   @Test
   public void checkinReturnsDaysLate() {
      addPatronJoe();
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(joeId, barCode, new Date());
      Date fiveDaysLate = DateUtil.addDays(service.dateDue(barCode), 5);

      int daysLate = service.checkIn(barCode, fiveDaysLate, EAST_SCAN);

      assertThat(daysLate, is(5));
   }

   @Test
   public void updatesFinesOnLateCheckIn() {
      addPatronJoe();
      String barCode = addHolding(THE_TRIAL);
      service.checkOut(joeId, barCode, new Date());

      Holding holding = service.find(barCode);
      Date oneDayLate = DateUtil.addDays(holding.dateDue(), 1);
      service.checkIn(barCode, oneDayLate, branchScanCode);

      assertEquals(MaterialType.Book.getDailyFine(), retrieve(joeId).fineBalance());
   }

   private Patron retrieve(String id) {
      return new PatronStore().find(id);
   }
}
