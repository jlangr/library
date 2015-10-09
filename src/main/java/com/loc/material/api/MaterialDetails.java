package com.loc.material.api;

public class MaterialDetails {
   private String title;
   private String author;
   private String year;
   private MaterialType format;
   private String classification;

   public MaterialDetails(String author, String title, String classification,
         MaterialType format, String year) {
      this.author = author;
      this.title = title;
      this.classification = classification;
      this.format = format;
      this.year = year;
   }

   public String getClassification() {
      return classification;
   }

   public String getTitle() {
      return title;
   }

   public String getAuthor() {
      return author;
   }

   public String getYear() {
      return year;
   }

   public MaterialType getFormat() {
      return format;
   }
}
