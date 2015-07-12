package reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.core.Book;
import domain.core.Catalog;
import domain.core.Holding;

public class InventoryReport {
   private static final String NEWLINE = System.getProperty("line.separator");
   private static final int SPACING = 2;
   private static final int TITLE_LENGTH = 20;
   private static final int BRANCH_LENGTH = 20;
   private static final int AUTHOR_LENGTH = 30;
   private static final int YEAR_LENGTH = 6;
   private static final int ISBN_LENGTH = 10;
   private Catalog catalog;
   private LibraryOfCongress congress;

   public InventoryReport(
         Catalog catalog, LibraryOfCongress libraryOfCongress) {
      this.catalog = catalog;
      this.congress = libraryOfCongress;
   }

   public InventoryReport(Catalog catalog) {
      this(catalog, new LibraryOfCongress());
   }

   public String allBooks() {
      List<Record> records = new ArrayList<Record>();
      for (Holding holding: catalog) {
         if (holding.getBook().getType() == Book.TYPE_BOOK) {
            records.add(new Record(holding));
         }
      }

      Collections.sort(records);

      StringBuffer buffer = new StringBuffer();
      appendHeader(buffer);
      appendColumnHeaders(buffer);

      for (Record record: records) {
         append(buffer, record);
      }

      return buffer.toString();
   }

   private void appendColumnHeaders(StringBuffer buffer) {
      buffer.append(pad("Title", TITLE_LENGTH + SPACING));
      buffer.append(pad("Branch", BRANCH_LENGTH + SPACING));
      buffer.append(pad("Author", AUTHOR_LENGTH + SPACING));
      buffer.append(pad("Year", YEAR_LENGTH));
      buffer.append(pad("ISBN", ISBN_LENGTH));
      buffer.append(NEWLINE);

      buffer.append(underlines(TITLE_LENGTH, SPACING));
      buffer.append(underlines(BRANCH_LENGTH, SPACING));
      buffer.append(underlines(AUTHOR_LENGTH, SPACING));
      buffer.append(underlines(YEAR_LENGTH, SPACING));
      buffer.append(NEWLINE);
   }

   private void append(StringBuffer buffer, Record record) {
      buffer.append(pad(record.title, TITLE_LENGTH));
      buffer.append(pad(SPACING));
      buffer.append(pad(record.branch, BRANCH_LENGTH));
      buffer.append(pad(SPACING));
      buffer.append(pad(record.author, AUTHOR_LENGTH));
      buffer.append(pad(SPACING));
      buffer.append(pad(record.year, YEAR_LENGTH));
      buffer.append(pad(SPACING));
      buffer.append(pad(record.isbn, ISBN_LENGTH));
      buffer.append(NEWLINE);
   }

   private String pad(int totalLength) {
      return pad("", totalLength);
   }

   private String pad(String text, int totalLength) {
      StringBuffer buffer = new StringBuffer(text);
      for (int i = 0; i < totalLength - text.length(); i++)
         buffer.append(' ');
      return buffer.toString();
   }

   private String underlines(int count, int spacing) {
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < count; i++)
         buffer.append('-');
      return pad(buffer.toString(), count + spacing);
   }

   private void appendHeader(StringBuffer buffer) {
      buffer.append("Inventory" + NEWLINE);
      buffer.append(NEWLINE);
   }

   class Record implements Comparable<Record> {
      String title;
      String branch;
      String author;
      String year;
      String isbn;

      public Record(Holding holding) {
         this.title = holding.getBook().getTitle();
         this.branch = holding.getBranch().getName();
         this.author = holding.getBook().getAuthor();
         this.year = holding.getBook().getYear();
         this.isbn = congress.getISBN(holding.getBook().getClassification());
      }

      @Override
      public int compareTo(Record that) {
         return this.title.compareTo(that.title);
      }
   }

   public void createFooter(StringBuffer buffer) {
      buffer.append("Made in China");
   }
}
