package persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import testutil.CollectionsUtil;
import domain.core.BookTestData;
import domain.core.Holding;

public class HoldingStoreTest {
   private HoldingStore store;
   private Holding savedHolding;

   @Before
   public void setUp() {
      HoldingStore.deleteAll();
      store = new HoldingStore();
      savedHolding = new Holding(BookTestData.THE_TRIAL);
      store.save(savedHolding);
   }

   @Test
   public void returnsAddedHoldings() {
      List<Holding> retrieved = store.findAll(savedHolding.getBook()
            .getClassification());

      assertEquals(savedHolding.getBook(),
            CollectionsUtil.soleElement(retrieved).getBook());
   }

   @Test
   public void returnsNewInstanceOnRetrieval() {
      store = new HoldingStore();

      List<Holding> retrieved = store.findAll(savedHolding.getBook()
            .getClassification());

      assertNotSame(savedHolding, CollectionsUtil.soleElement(retrieved));
   }

   @Test
   public void getAllReturnsAllSavedHoldings() {
      Holding holding2 = new Holding(BookTestData.AGILE_JAVA);
      store.save(holding2);

      Collection<Holding> retrieved = store.getAll();

      CollectionsUtil.containsExactly(retrieved, savedHolding, holding2);
   }

   @Test
   public void findBarCodeReturnsMatchingHolding() {
      String barCode = Holding.createBarCode(BookTestData.KAFKA_CLASSIFICATION,
            1);

      Holding holding = store.find(barCode);

      assertEquals(BookTestData.THE_TRIAL, holding.getBook());
      assertEquals(barCode, holding.getBarCode());
   }

   @Test
   public void findBarCodeNotFound() {
      assertNull(store.find("nonexistent barcode:1"));
   }
}