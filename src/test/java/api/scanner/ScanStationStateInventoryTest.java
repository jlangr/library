package api.scanner;

import static api.scanner.ScanStationStateInventory.*;
import static api.scanner.ScanStationTestData.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import org.junit.Test;
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

   @Test
   public void changesBranchWhenBranchIdScanned() {
      assertThat(scanner.getBranchId(), not(equalTo(BRANCH_WEST_ID)));
      when(branchService.find(BRANCH_WEST_ID)).thenReturn(
            new Branch("West", BRANCH_WEST_ID));

      state.scanBranchId(BRANCH_WEST_ID);

      assertThat(scanner.getBranchId(), equalTo(BRANCH_WEST_ID));
      assertMessageDisplayed(String.format(ScanStation.MSG_BRANCH_SET_TO,
            "West"));
      assertStateUnchanged();
   }

   @Test
   public void addsNewHoldingToLibraryWhenSourceIdScanned() {
      String sourceId = "1234567890123";
      scanner.setBranch(BRANCH_WEST);

      state.scanHolding(sourceId);

      verify(holdingService).add(sourceId, BRANCH_WEST_ID);
      assertStateUnchanged();
      assertMessageDisplayed(String.format(MSG_SCANNED_HOLDING, sourceId));
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
