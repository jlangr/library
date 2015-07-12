package api.scanner;

public interface ScanStationState {
   void scanBranchId(String barcode);

   void scanHolding(String barcode);

   void scanPatron(String barcode);

   void scanInventoryCard();

   void pressComplete();
}
