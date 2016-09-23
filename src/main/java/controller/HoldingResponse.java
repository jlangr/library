package controller;

import com.loc.material.api.*;
import domain.core.Holding;

public class HoldingResponse {

   private String author;
   private String title;
   private String year;
   private String format;

   public HoldingResponse(Holding holding) {
      holding.getBarCode();
      holding.getCopyNumber();

      MaterialDetails material = holding.getMaterial();
      author = material.getAuthor();
      title = material.getTitle();
      year = material.getYear();
      format = material.getFormat().toString();
   }

   public String getAuthor() {
      return author;
   }

   public String getTitle() {
      return title;
   }

   public String getYear() {
      return year;
   }

   public String getFormat() {
      return format;
   }
}
