package api.scanner;

import static api.scanner.ScanStationStateInventory.*;
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
      when(branchService.find("b222")).thenReturn(new Branch("b222", "West"));

      state.scanBranchId("b222");

      assertThat(scanner.getBranchId(), equalTo("b222"));
      assertMessageDisplayed(String.format(ScanStation.MSG_BRANCH_SET_TO, "West"));
      assertStateUnchanged();
   }

   @Test
   public void addsNewHoldingToLibraryWhenSourceIdScanned() {
      String sourceId = "1234567890123";
      scanner.setBranch(new Branch("b123", ""));

      state.scanHolding(sourceId);

      verify(holdingService).add(sourceId, "b123");
      assertStateUnchanged();
      assertMessageDisplayed(String.format(MSG_SCANNED_HOLDING, sourceId));
   }

   @Test
   public void displaysWarningWhenPatronCardScanned() {
      state.scanPatron("p123");

      assertStateRetainedWithMessage(MSG_COMPLETE_INVENTORY_FIRST);
   }

   @Test
   public void changesStateToReturnsWhenCompletePressed() {
      state.pressComplete();

      assertThat(scanner.getCurrentState(), is(instanceOf(ScanStationStateReturns.class)));
   }
}
