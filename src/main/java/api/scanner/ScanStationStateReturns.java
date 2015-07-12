package api.scanner;

import java.util.Date;

import util.TimestampSource;

public class ScanStationStateReturns extends AbstractScanStationState implements
      ScanStationState {
   public static final String MSG_WAITING_FOR_RETURNS = "Waiting for returns";
   public static final String MSG_INVENTORY = "Entering into inventory mode for current branch";
   public static final String MSG_CHECKED_IN = "Checked in %s";
   private final ScanStation scanStation;

   public ScanStationStateReturns(ScanStation scanStation) {
      this.scanStation = scanStation;
   }

   @Override
   public void scanBranchId(String barcode) {
      scanStation.scanBranchId(barcode);
   }

   @Override
   public void scanHolding(String holdingId) {
      Date checkinDate = TimestampSource.now();

      scanStation.showMessage(String.format(MSG_CHECKED_IN, holdingId));
      scanStation.getLibrarySystem().checkIn(holdingId, checkinDate,
            scanStation.getBranchId());
   }

   @Override
   public void scanPatron(String patronId) {
      scanStation.scanPatronId(patronId);
      scanStation.setCurrentState(new ScanStationStateCheckout(scanStation));
   }

   @Override
   public void scanInventoryCard() {
      scanStation.setCurrentState(new ScanStationStateInventory(scanStation));
      scanStation.showMessage(MSG_INVENTORY);
   }

   @Override
   public void pressComplete() {
      scanStation.showMessage(MSG_WAITING_FOR_RETURNS);
   }
}
