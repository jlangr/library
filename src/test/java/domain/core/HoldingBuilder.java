package domain.core;

import com.loc.material.api.MaterialDetails;

class HoldingBuilder {
   private MaterialDetails material = new MaterialDetails("1", "", "", "", "");
   private int copyNumber = 1;
   private Branch branch = Branch.CHECKED_OUT;

   public HoldingBuilder with(MaterialDetails material) {
      this.material = material;
      return this;
   }

   public HoldingBuilder withCopy(int copyNumber) {
      this.copyNumber = copyNumber;
      return this;
   }

   public HoldingBuilder atBranch(Branch branch) {
      this.branch = branch;
      return this;
   }

   public Holding create() {
      return new Holding(material, branch, copyNumber);
   }
}