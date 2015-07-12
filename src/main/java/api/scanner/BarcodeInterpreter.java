package api.scanner;

public class BarcodeInterpreter {

   public static BarcodeType typeOf(String barcode) {
      if (isBranchId(barcode))
         return BarcodeType.Branch;
      if (isInventoryId(barcode))
         return BarcodeType.Inventory;
      if (isHoldingId(barcode))
         return BarcodeType.Holding;
      if (isPatronId(barcode))
         return BarcodeType.Patron;
      return BarcodeType.Unrecognized;
   }

   private static boolean isPatronId(String barcode) {
      return barcode.startsWith("p");
   }

   private static boolean isHoldingId(String barcode) {
      return barcode.indexOf(':') != -1;
   }

   private static boolean isInventoryId(String barcode) {
      return barcode.toLowerCase().startsWith("i");
   }

   public static boolean isBranchId(String barcode) {
      return barcode.toLowerCase().startsWith("b");
   }

}
