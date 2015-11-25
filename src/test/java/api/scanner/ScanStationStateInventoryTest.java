package api.scanner;

import static api.scanner.ScanStationStateInventory.MSG_COMPLETE_INVENTORY_FIRST;
import static api.scanner.ScanStationStateInventory.MSG_SCANNED_HOLDING;
import static api.scanner.ScanStationTestData.BRANCH_WEST;
import static api.scanner.ScanStationTestData.BRANCH_WEST_ID;
import static api.scanner.ScanStationTestData.PATRON_JOE_ID;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.*;

import domain.core.Branch;

public class ScanStationStateInventoryTest extends ScanStationStateTestBase {
   @Override
   protected ScanStationStateInventory createStateObject() {
      return new ScanStationStateInventory(scanner);
   }

   @Test
   public void displaysWarningWhenInventoryCardScanned() {
      state.scanInventoryCard();

      assertStateRetainedWithMessage(MSG_COMPLETE_INVENTORY_FIRST);
   }

   private void assertStateRetainedWithMessage(String message) {
      assertMessageDisplayed(message);
      assertStateUnchanged();
   }

//   @Test
//   public void changesBranchWhenBranchIdScanned() {
//      assertThat(scanner.getBranchId(), is(not(BRANCH_WEST_ID)));
//      when(branchService.find(BRANCH_WEST_ID)).thenReturn(
//            new Branch("West", BRANCH_WEST_ID));
//
//      state.scanBranchId(BRANCH_WEST_ID);
//
//      assertThat(scanner.getBranchId(), is(BRANCH_WEST_ID));
//      assertMessageDisplayed(String.format(ScanStation.MSG_BRANCH_SET_TO,
//            "West"));
//      assertStateUnchanged();
//   }

   @Test
   public void changesToInventoryReconciliationStateWhenBranchIdScanned() {
      assertThat(scanner.getBranchId(), is(not(BRANCH_WEST_ID)));
      Branch west = new Branch("West");
      west.setScanCode(BRANCH_WEST_ID);
      when(branchService.find(BRANCH_WEST_ID)).thenReturn(west);

      state.scanBranchId(BRANCH_WEST_ID);

      assertThat(scanner.getBranchId(), is(BRANCH_WEST_ID));
      assertCurrentState(ScanStationStateReconciliation.class);
   }

   
   @Test
   public void addsNewHoldingToLibraryWhenHoldingBarcodeScanned() {
      String tessBarcode = "123:1";
      scanner.setBranch(BRANCH_WEST);

      state.scanHolding(tessBarcode);

      verify(holdingService).add(tessBarcode, BRANCH_WEST_ID);
      assertStateUnchanged();
      assertMessageDisplayed(String.format(MSG_SCANNED_HOLDING, tessBarcode));
   }

   @Test
   public void displaysWarningWhenPatronCardScanned() {
      state.scanPatron(PATRON_JOE_ID);

      assertStateRetainedWithMessage(MSG_COMPLETE_INVENTORY_FIRST);
   }

   @Test
   public void changesStateToReturnsWhenCompletePressed() {
      state.pressComplete();

      assertThat(scanner.getCurrentState(),
            is(instanceOf(ScanStationStateReturns.class)));
   }
}
