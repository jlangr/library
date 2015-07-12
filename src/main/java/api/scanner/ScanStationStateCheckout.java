package api.scanner;

import java.util.Date;

import util.TimestampSource;
import domain.core.Holding;

public class ScanStationStateCheckout extends AbstractScanStationState
      implements ScanStationState {
   public static final String MSG_COMPLETE_CHECKOUT_FIRST = "Please complete checking out current patron first.";
   public static final String MSG_CHECKED_OUT = "Checked out %s";
   public static final String MSG_COMPLETED_CHECKOUT = "Completed checkout for %s";
   public static final String MSG_ALREADY_CHECKED_OUT = "%s is already checked out";
   public static final String MSG_INVALID_HOLDING_ID = "Invalid barcode %s--cannot check out";

   private final ScanStation scanner;

   public ScanStationStateCheckout(ScanStation scanner) {
      this.scanner = scanner;
   }

   @Override
   public void scanBranchId(String branchId) {
      scanner.showMessage(MSG_COMPLETE_CHECKOUT_FIRST);
   }

   @Override
   public void scanHolding(String holdingId) {
      Date checkoutTime = TimestampSource.now();

      Holding holding = scanner.getLibrarySystem().find(holdingId);
      if (holding == null) {
         scanner.showMessage(String.format(MSG_INVALID_HOLDING_ID, holdingId));
         return;
      }
      if (!holding.isAvailable()) {
         scanner.showMessage(String.format(MSG_ALREADY_CHECKED_OUT, holdingId));
         return;
      }
      scanner.getLibrarySystem().checkOut(scanner.getPatronId(), holdingId,
            checkoutTime);
      scanner.showMessage(String.format(MSG_CHECKED_OUT, holdingId));
   }

   @Override
   public void scanPatron(String patronId) {
      scanner.showMessage(MSG_COMPLETE_CHECKOUT_FIRST);
   }

   @Override
   public void scanInventoryCard() {
      scanner.showMessage(MSG_COMPLETE_CHECKOUT_FIRST);
   }

   @Override
   public void pressComplete() {
      scanner.setCurrentState(new ScanStationStateReturns(scanner));
      scanner.showMessage(String.format(MSG_COMPLETED_CHECKOUT,
            scanner.getPatronId()));
   }
}
