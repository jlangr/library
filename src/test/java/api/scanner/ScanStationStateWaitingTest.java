package api.scanner;

import static api.scanner.ScanStationStateWaiting.MSG_SCAN_BRANCH_ID_FIRST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import org.junit.Test;
import domain.core.Branch;

public class ScanStationStateWaitingTest extends ScanStationStateTestBase {
   @Override
   protected ScanStationState createStateObject() {
      return new ScanStationStateWaiting(scanner);
   }

   @Test
   public void storesBranchIdWhenBranchCardScanned() {
      Branch westBranch = new Branch("b123", "west");
      when(branchService.find("b123")).thenReturn(westBranch);
      scanner.setBranch(new Branch("b999", "other"));

      state.scanBranchId("b123");

      assertCurrentState(ScanStationStateReturns.class);
      assertMessageDisplayed(String.format(ScanStation.MSG_BRANCH_SET_TO, westBranch.getName()));
      assertThat(scanner.getBranchId(), equalTo("b123"));
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