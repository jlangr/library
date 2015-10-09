package domain.core;

public class Material {
   public static final int BOOK_CHECKOUT_PERIOD = 21; // days
   public static final int MOVIE_CHECKOUT_PERIOD = 7;
   public static final int NEW_RELEASE_CHECKOUT_PERIOD = 7;

   public static final int TYPE_BOOK = 0;
   public static final int TYPE_MOVIE = 1;
   public static final int TYPE_NEW_RELEASE = 2;

   public static final int BOOK_DAILY_FINE = 10;
   public static final int MOVIE_DAILY_FINE = 100;
   public static final int NEW_RELEASE_DAILY_FINE = 20;

   private String author;
   private String title;
   private String classification;
   private String year;
   private int type;

   public Material(String author, String title, String classification, String year) {
      this(author, title, classification, year, Material.TYPE_BOOK);
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
