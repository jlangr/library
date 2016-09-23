package api.library;

import static domain.core.BranchTest.BRANCH_EAST;
import static domain.core.MaterialTestData.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Date;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;

public class HoldingService_WithBranchCreatedTest {
   private HoldingService service = new HoldingService();
   private ClassificationApi classificationApi = mock(ClassificationApi.class);
   private MaterialDetails material = THE_TRIAL;
   private String branchScanCode;

   @Before
   public void initialize() {
      // TODO remove need to persist!
      LibraryData.deleteAll();
      ClassificationApiFactory.setService(classificationApi);
      branchScanCode = new BranchService().add("a branch name");
   }

   private String addHolding() {
      when(classificationApi.getMaterialDetails(material.getSourceId())).thenReturn(material);
      return service.add(material.getSourceId(), branchScanCode);
   }

   @Test
   public void returnsEntireInventoryOfHoldings() {
      for (int i = 0; i < 3; i++)
         addHolding();

      HoldingMap holdings = service.allHoldings();

      assertEquals(3, holdings.size());
   }

   @Test
   public void storesNewHoldingAtBranch() {
      String barcode = addHolding();

      assertEquals(branchScanCode, service.find(barcode).getBranch().getScanCode());
   }

   @Test
   public void findByBarCodeReturnsNullWhenNotFound() {
      assertThat(service.find("999:1"), nullValue());
   }

   @Test
   public void updatesBranchOnHoldingTransfer() {
      String barcode = addHolding();
      Holding holding = service.find(barcode);

      // TODO change to take a barcode
      service.transfer(holding, BRANCH_EAST);

      holding = service.find(barcode);
      assertEquals(BRANCH_EAST, holding.getBranch());
   }

   @Test
   public void holdingIsAvailableWhenNotCheckedOut() {
      String barcode = addHolding();

      assertTrue(service.isAvailable(barcode));
   }

   @Test(expected = HoldingNotFoundException.class)
   public void availabilityCheckThrowsWhenHoldingNotFound() {
      service.isAvailable("345:1");
   }

   @Test(expected = HoldingNotFoundException.class)
   public void checkinThrowsWhenHoldingIdNotFound() {
      service.checkIn("999:1", new Date(), branchScanCode);
   }
}