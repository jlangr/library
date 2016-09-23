package controller;

public class AddHoldingRequest {
   private String holdingBarcode;
   private String branchId;

   public String getSourceId() {
      return holdingBarcode;
   }
   public String getBranchId() {
      return branchId;
   }

   public void setHoldingBarcode(String holdingBarcode) {
      this.holdingBarcode = holdingBarcode;
   }

   public void setBranchId(String branchId) {
      this.branchId = branchId;
   }
}
