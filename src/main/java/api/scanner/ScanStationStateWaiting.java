package api.scanner;

public class ScanStationStateWaiting extends AbstractScanStationState implements
      ScanStationState {
   final ScanStation scanStation;
   public static final String MSG_SCAN_BRANCH_ID_FIRST = "Scan branch ID card to continue.";

   public ScanStationStateWaiting(ScanStation scanStation) {
      this.scanStation = scanStation;
   }

   @Override
   public void scanBranchId(String barcode) {
      scanStation.setCurrentState(new ScanStationStateReturns(scanStation));
      scanStation.scanBranchId(barcode);
   }

   @Override
   public void scanHolding(String barcode) {
      scanStation.showMessage(MSG_SCAN_BRANCH_ID_FIRST);
   }

   @Override
   public void scanPatron(String barcode) {
      scanStation.showMessage(MSG_SCAN_BRANCH_ID_FIRST);
   }

   @Override
   public void scanInventoryCard() {
      scanStation.showMessage(MSG_SCAN_BRANCH_ID_FIRST);
   }

   @Override
   public void pressComplete() {
      scanStation.showMessage(MSG_SCAN_BRANCH_ID_FIRST);
   }
}
