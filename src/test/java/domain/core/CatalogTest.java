package domain.core;

import static domain.core.BookTestData.AGILE_JAVA;
import static domain.core.BookTestData.KAFKA_CLASSIFICATION;
import static domain.core.BookTestData.LANGR_CLASSIFICATION;
import static domain.core.BookTestData.THE_TRIAL;
import static domain.core.BranchTest.BRANCH_EAST;
import static domain.core.BranchTest.BRANCH_WEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import persistence.HoldingStore;

public class CatalogTest {
   private Catalog catalog;

   @Before
   public void initialize() {
      catalog = new Catalog();
      HoldingStore.deleteAll();
   }

   @Test
   public void isEmptyOnCreation() {
      assertEquals(0, catalog.size());
   }

   @Test
   public void incrementsSizeWhenBookAdded() {
      catalog.add(new Holding(BookTestData.THE_TRIAL, BRANCH_EAST));

      assertEquals(1, catalog.size());
   }

   @Test
   public void answersEmptyForNonexistentBook() {
      assertTrue(catalog.findAll(KAFKA_CLASSIFICATION).isEmpty());
   }

   @Test
   public void findAllReturnsListOfHoldings() {
      Holding holding1 = new Holding(THE_TRIAL, BRANCH_EAST);
      Holding holding2 = new Holding(THE_TRIAL, BRANCH_EAST);
      catalog.add(holding1);
      catalog.add(holding2);

      List<Holding> holdings = catalog.findAll(KAFKA_CLASSIFICATION);

      assertEquals(2, holdings.size());
      assertEquals(holding1, holdings.get(0));
      assertEquals(holding2, holdings.get(1));
   }

   @Test
   public void findAllReturnsOnlyHoldingsWithMatchingClassification() {
      catalog.add(new Holding(THE_TRIAL, BRANCH_EAST));
      catalog.add(new Holding(AGILE_JAVA, BRANCH_EAST));

      List<Holding> retrievedForClassification1 = catalog
            .findAll(KAFKA_CLASSIFICATION);
      List<Holding> retrievedForClassification2 = catalog
            .findAll(LANGR_CLASSIFICATION);

      HoldingTest.assertContains(retrievedForClassification1, THE_TRIAL);
      HoldingTest.assertContains(retrievedForClassification2, AGILE_JAVA);
   }

   @Test
   public void findBarCode() {
      Holding holding = new Holding(THE_TRIAL, BRANCH_EAST);
      catalog.add(holding);

      Holding retrieved = catalog.find(holding.getBarCode());

      assertEquals(holding, retrieved);
   }

   @Test
   public void supportsIteration() {
      Holding holding1 = new Holding(THE_TRIAL, BRANCH_EAST, 1);
      Holding holding2 = new Holding(THE_TRIAL, BRANCH_WEST, 2);
      catalog.add(holding1);
      catalog.add(holding2);

      Set<Holding> results = new HashSet<Holding>();

      for (Holding holding: catalog)
         results.add(holding);

      assertEquals(2, results.size());
      assertTrue(results.contains(holding1));
      assertTrue(results.contains(holding2));
   }
}