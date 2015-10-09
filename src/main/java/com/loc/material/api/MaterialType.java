package com.loc.material.api;

import domain.core.*;

public enum MaterialType {
   Book(21, 10),
   AudioCassette(14, 10),
   VinylRecording(14, 10),
   MicroFiche(7, 200),
   AudioCD(7, 100),
   SoftwareCD(7, 500),
   DVD(3, 100),
   NewReleaseDVD(1, 200),
   BluRay(3, 200),
   VideoCassette(7, 10);

   private int checkoutPeriod;
   private int dailyFine;
   public static final int TYPE_NEW_RELEASE = 2;
   public static final int TYPE_MOVIE = 1;
   public static final int TYPE_BOOK = 0;

   public static MaterialType from(int value) {
      switch(value) {
         case MaterialType.TYPE_BOOK:
            return MaterialType.Book;
         case MaterialType.TYPE_MOVIE:
            return MaterialType.DVD;
         case MaterialType.TYPE_NEW_RELEASE:
            return MaterialType.DVD;
      }
      return MaterialType.Book;
   }

   MaterialType(int checkoutPeriod) {
      this(checkoutPeriod, 10);
   }

   MaterialType(int checkoutPeriod, int dailyFine) {
      this.checkoutPeriod = checkoutPeriod;
      this.dailyFine = dailyFine;
   }

   public int getCheckoutPeriod() {
      return checkoutPeriod;
   }

   public int getDailyFine() {
      return dailyFine;
   }

   public int getType() {
      switch (this) {
         case Book: return MaterialType.TYPE_BOOK;
         case DVD: return MaterialType.TYPE_MOVIE;
         case NewReleaseDVD: return MaterialType.TYPE_NEW_RELEASE;
      }
      return 0;
   }
}
