package domain.core;

import com.loc.material.api.*;

public class Material {
   private String author;
   private String title;
   private String classification;
   private String year;
   private int type;

   public Material(String author, String title, String classification, String year) {
      this(author, title, classification, year, MaterialType.TYPE_BOOK);
   }

   public Material(String author, String title, String classification, String year,
         int type) {
      this.author = author;
      this.title = title;
      this.classification = classification;
      this.year = year;
      this.type = type;
   }

   public String getClassification() {
      return classification;
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

   public int getType() {
      return type;
   }

   @Override
   public String toString() {
      return author + " " + title + " " + year + " " + classification;
   }
}
