package api.scanner;

import static api.scanner.ScanStationStateWaiting.MSG_SCAN_BRANCH_ID_FIRST;
import static api.scanner.ScanStationTestData.BRANCH_EAST;
import static api.scanner.ScanStationTestData.BRANCH_WEST;
import static api.scanner.ScanStationTestData.BRANCH_WEST_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ScanStationStateWaitingTest extends ScanStationStateTest {
   @Override
   protected ScanStationState createStateObject() {
      return new ScanStationStateWaiting(scanner);
   }

   @Test
   public void storesBranchIdWhenBranchCardScanned() {
      when(branchService.find(BRANCH_WEST_ID)).thenReturn(BRANCH_WEST);
      scanner.setBranch(BRANCH_EAST);

      state.scanBranchId(BRANCH_WEST_ID);

      assertCurrentState(ScanStationStateReturns.class);
      assertMessageDisplayed(String.format(ScanStation.MSG_BRANCH_SET_TO,
            BRANCH_WEST.getName()));
      assertThat(scanner.getBranchId(), is(BRANCH_WEST_ID));
   }

   @Test
   public void displaysWarningMessageOnWhenHoldingScanned() {
      state.scanHolding("");

      assertStateUnchanged();
      assertMessageDisplayed(MSG_SCAN_BRANCH_ID_FIRST);
   }

   @Test
   public void displaysWarningMessageOnWhenInventoryCardScanned() {
      state.scanInventoryCard();

      assertStateUnchanged();
      assertMessageDisplayed(MSG_SCAN_BRANCH_ID_FIRST);
   }

   @Test
   public void displaysWarningMessageOnWhenPatronScanned() {
      state.scanPatron("");

      assertStateUnchanged();
      assertMessageDisplayed(MSG_SCAN_BRANCH_ID_FIRST);
   }

   @Test
   public void displaysWarningMessageOnWhenCompletePressed() {
      state.pressComplete();

      assertStateUnchanged();
      assertMessageDisplayed(MSG_SCAN_BRANCH_ID_FIRST);
   }
}
