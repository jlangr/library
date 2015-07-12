package api.scanner;

import java.util.*;

public class ScanStationStateReconciliation extends AbstractScanStationState
implements ScanStationState {

   public static final String MSG_COMPLETE_INVENTORY_RECONCILIATION_FIRST = "Please complete reconciliation.";
   private final ScanStation scanStation;
   private List<String> holdings = new ArrayList<String>();

   public ScanStationStateReconciliation(ScanStation scanStation) {
      this.scanStation = scanStation;
   }

   @Override
   public void scanBranchId(String barcode) {
      showWarning();
   }

   @Override
   public void scanHolding(String barcode) {
      holdings.add(barcode);
   }

   @Override
   public void scanPatron(String barcode) {
      showWarning();
   }

   private void showWarning() {
      scanStation.showMessage(MSG_COMPLETE_INVENTORY_RECONCILIATION_FIRST);
   }

   @Override
   public void scanInventoryCard() {
      showWarning();
   }

   @Override
   public void pressComplete() {
      scanStation.setCurrentState(new ScanStationStateInventory(scanStation));
   }

   public List<String> getScannedHoldings() {
      return holdings;
   }
}
