package domain.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BookTest {

   @Test
   public void booksAreOfBookType() {
      Book book = new Book("", "", "", "");
      assertEquals(Book.TYPE_BOOK, book.getType());
   }

   public static void assertBookEquals(Book expectedBook, Book book) {
      assertEquals(expectedBook.getAuthor(), book.getAuthor());
      assertEquals(expectedBook.getClassification(), book.getClassification());
      assertEquals(expectedBook.getTitle(), book.getTitle());
      assertEquals(expectedBook.getYear(), book.getYear());
   }
}