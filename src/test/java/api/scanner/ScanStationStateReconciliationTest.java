package api.scanner;

import static api.scanner.ScanStationTestData.*;
import java.util.*;
import org.junit.*;
import testutil.*;

public class ScanStationStateReconciliationTest extends ScanStationStateTest {

   @Override
   protected ScanStationState createStateObject() {
      return new ScanStationStateReconciliation(scanner);
   }

   @Test
   public void displaysWarningWhenBranchIdScanned() {
      state.scanBranchId(BRANCH_EAST_ID);

      assertMessageDisplayed(ScanStationStateReconciliation.MSG_COMPLETE_INVENTORY_RECONCILIATION_FIRST);
      assertStateUnchanged();
   }

   @Test
   public void displaysWarningWhenPatronScanned() {
      state.scanPatron(PATRON_JANE_ID);

      assertMessageDisplayed(ScanStationStateReconciliation.MSG_COMPLETE_INVENTORY_RECONCILIATION_FIRST);
      assertStateUnchanged();
   }

   @Test
   public void displaysWarningWhenInventoryIdScanned() {
      state.scanInventoryCard();

      assertMessageDisplayed(ScanStationStateReconciliation.MSG_COMPLETE_INVENTORY_RECONCILIATION_FIRST);
      assertStateUnchanged();
   }

   @Test
   public void collectsBooksWhenHoldingScanned() {
      state.scanHolding(HOLDING_ANIMAL_FARM);

      List<String> holdings = ((ScanStationStateReconciliation)state).getScannedHoldings();

      CollectionsUtil.assertSoleElement(holdings, HOLDING_ANIMAL_FARM);
      assertStateUnchanged();
   }

   @Test
   public void returnsToInventoryStateWhenCompletePressed() {
      state.pressComplete();

      assertCurrentState(ScanStationStateInventory.class);
   }
}
