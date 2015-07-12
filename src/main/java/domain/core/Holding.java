package domain.core;

import java.util.Calendar;
import java.util.Date;
import util.*;

public class Holding {
   public static final String BARCODE_SEPARATOR = ":";
   private Book book;
   private Branch branch;
   private Date dateCheckedOut;
   private Date dateLastCheckedIn;
   private int copyNumber;

   public Holding(Book book) {
      this(book, Branch.CHECKED_OUT);
   }

   public Holding(Book book, Branch branch) {
      this(book, branch, 1);
   }

   public Holding(Book book, Branch branch, int copyNumber) {
      this.book = book;
      this.branch = branch;
      this.copyNumber = copyNumber;
   }

   public Book getBook() {
      return book;
   }

   public Branch getBranch() {
      return branch;
   }

   public int getCopyNumber() {
      return copyNumber;
   }

   public void transfer(Branch newBranch) {
      branch = newBranch;
   }

   public boolean isAvailable() {
      return branch != Branch.CHECKED_OUT;
   }

   public void checkOut(Date date) {
      branch = Branch.CHECKED_OUT;
      dateCheckedOut = date;
   }

   public Date dateCheckedOut() {
      return dateCheckedOut;
   }

   public Date dateDue() {
      return addDays(dateCheckedOut, getHoldingPeriod());
   }

   private Date addDays(Date date, int days) {
      Calendar calendar = Calendar.getInstance();
      final long msInDay = 1000L * 60 * 60 * 24;
      calendar.setTime(new Date(date.getTime() + msInDay * days));
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar.getTime();
   }

   private int getHoldingPeriod() {
      int period = 0;

      switch (book.getType()) {
         case Book.TYPE_BOOK:
            period = Book.BOOK_CHECKOUT_PERIOD;
            break;
         case Book.TYPE_MOVIE:
            period = Book.MOVIE_CHECKOUT_PERIOD;
            break;
         case Book.TYPE_NEW_RELEASE:
            period = Book.NEW_RELEASE_CHECKOUT_PERIOD;
            break;
         default:
            period = Book.BOOK_CHECKOUT_PERIOD;
            break;
      }
      return period;
   }

   public int checkIn(Date date, Branch branch) {
      dateLastCheckedIn = date;
      this.branch = branch;
      int daysLate = DateUtil.daysFrom(dateDue(), dateLastCheckedIn);
      return daysLate < 0 ? 0 : daysLate;
   }

   public Date dateLastCheckedIn() {
      return dateLastCheckedIn;
   }

   public String getBarCode() {
      return createBarCode(book.getClassification(), copyNumber);
   }

   public void setCopyNumber(int copyNumber) {
      this.copyNumber = copyNumber;
   }

   public static String createBarCode(String classification, int copyNumber) {
      return classification + BARCODE_SEPARATOR + copyNumber;
   }

   @Override
   public boolean equals(Object object) {
      if (object == null)
         return false;
      if (this.getClass() != object.getClass())
         return false;
      Holding that = (Holding)object;
      return this.getBarCode().equals(that.getBarCode());
   }

   @Override
   public String toString() {
      return book.toString() + "(" + copyNumber + ") @ " + branch.getName();
   }

   @Override
   public int hashCode() {
      return getBarCode().hashCode();
   }

   public static int getCopyNumber(String barcode) {
      String copy = splitOnColon(barcode)[1];
      return parsePositiveInt(copy);
   }

   public static String getClassification(String barcode) {
      return splitOnColon(barcode)[0];
   }

   private static int parsePositiveInt(String text) {
      int number = parseInt(text);
      if (number < 1)
         throw new IllegalArgumentException();
      return number;
   }

   private static String[] splitOnColon(String barcode) {
      String[] barcodeParts = barcode.split(":");
      if (barcodeParts.length != 2)
         throw new IllegalArgumentException();
      return barcodeParts;
   }

   private static int parseInt(String text) {
      try {
         return Integer.parseInt(text, 10);
      } catch (Throwable t) {
         throw new IllegalArgumentException();
      }
   }

   public int daysLate() {
      return DateUtil.daysAfter(dateDue(), dateLastCheckedIn());
   }
}