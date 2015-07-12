package com.loc.material.api;

public class MaterialTestData {

   public static MaterialDetails createTess(String classification) {
      return new MaterialDetails("Hardy, Thomas", "Tess of the d'Urbervilles",
            classification, MaterialType.Book, "1891");
   }

}
