package reporting;

import org.junit.Test;

import domain.core.BookTestData;
import domain.core.Catalog;
import domain.core.Holding;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InventoryReportTest {
   @Test
   public void createReport() {
      Catalog catalog = new Catalog();
      catalog.add(new Holding(BookTestData.AGILE_JAVA));
      catalog.add(new Holding(BookTestData.DR_STRANGELOVE));
      catalog.add(new Holding(BookTestData.THE_TRIAL));

      LibraryOfCongress libraryOfCongress = 
         mock(LibraryOfCongress.class);
      when(libraryOfCongress.getISBN(anyString())).thenReturn("ISBNxxx");
      
//      InventoryReport report = 
         new InventoryReport(catalog, libraryOfCongress);
      
//      System.out.println(report.allBooks());
   }
   
   @Test
   public void createFooter() {
      InventoryReport report = new InventoryReport(null);
      StringBuffer buffer = new StringBuffer();
      
      report.createFooter(buffer);
      
      assertThat(buffer.toString(), is("Made in China"));
   }
}
