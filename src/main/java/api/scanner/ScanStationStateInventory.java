package api.scanner;

public class ScanStationStateInventory extends AbstractScanStationState
      implements ScanStationState {
   public static final String MSG_COMPLETE_INVENTORY_FIRST = "Please complete inventory process first.";
   public static final String MSG_SCANNED_HOLDING = "Scanned holding %s";

   private final ScanStation scanStation;

   public ScanStationStateInventory(ScanStation scanStation) {
      this.scanStation = scanStation;
   }

   @Override
   public void scanBranchId(String barcode) {
      scanStation.scanBranchId(barcode);
   }

   @Override
   public void scanHolding(String barcode) {
      String holdingId = barcode;
      scanStation.getLibrarySystem().add(holdingId, scanStation.getBranchId());
      scanStation.showMessage(String.format(MSG_SCANNED_HOLDING, holdingId));
   }

   @Override
   public void scanPatron(String barcode) {
      scanStation.showMessage(MSG_COMPLETE_INVENTORY_FIRST);
   }

   @Override
   public void scanInventoryCard() {
      scanStation.showMessage(MSG_COMPLETE_INVENTORY_FIRST);
   }

   @Override
   public void pressComplete() {
      scanStation.setCurrentState(new ScanStationStateReturns(scanStation));
   }
}
