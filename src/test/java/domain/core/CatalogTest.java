package domain.core;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static util.matchers.HasExactlyItems.hasExactlyItems;
import java.util.*;
import org.junit.*;
import com.loc.material.api.MaterialDetails;
import persistence.HoldingStore;

public class CatalogTest {
   private Catalog catalog = new Catalog();
   private HoldingBuilder holdingBuilder = new HoldingBuilder();

   @Before
   public void initialize() {
      HoldingStore.deleteAll();
   }

   @Test
   public void isEmptyOnCreation() {
      assertThat(catalog.size(), equalTo(0));
   }

   @Test
   public void incrementsSizeWhenMaterialAdded() {
      catalog.add(holdingBuilder.create());

      assertThat(catalog.size(), equalTo(1));
   }

   @Test
   public void answersEmptyForNonexistentMaterial() {
      assertTrue(catalog.findAll("nonexistentid").isEmpty());
   }

   @Test
   public void findAllReturnsListOfHoldings() {
      String classification = "123";
      String barcode = addHoldingWithClassification(classification);
      String barcode2 = addHoldingWithClassification(classification);

      List<Holding> holdings = catalog.findAll(classification);

      Holding retrieved1 = catalog.find(barcode);
      Holding retrieved2 = catalog.find(barcode2);
      assertThat(holdings, equalTo(asList(retrieved1, retrieved2)));
   }

   private String addHoldingWithClassification(String classification) {
      MaterialDetails material = new MaterialDetails("", "", "", classification, "");
      Holding holding = holdingBuilder.with(material).create();
      return catalog.add(holding);
   }

   @Test
   public void findAllReturnsOnlyHoldingsWithMatchingClassification() {
      String barcode1 = addHoldingWithClassification("123");
      addHoldingWithClassification("456");

      List<Holding> retrieved = catalog.findAll("123");

      assertThat(retrieved.size(), equalTo(1));
      assertThat(retrieved.get(0).getBarcode(), equalTo(barcode1));
   }

   @Test
   public void retrievesHoldingUsingBarcode() {
      Holding holding = holdingBuilder.create();
      String barcode = catalog.add(holding);

      Holding retrieved = catalog.find(barcode);

      assertThat(retrieved, equalTo(holding));
   }

   @Test
   public void incrementsCopyNumberWhenSameClassificationExists() {
      Holding holding = holdingBuilder.create();
      catalog.add(holding);
      String barcode = catalog.add(holding);

      Holding retrieved = catalog.find(barcode);

      assertThat(retrieved.getCopyNumber(), equalTo(2));
   }

   @Test
   public void supportsIteration() {
      String barcode1 = addHoldingWithClassification("1");
      String barcode2 = addHoldingWithClassification("2");

      List<String> results = new ArrayList<>();
      for (Holding holding: catalog)
         results.add(holding.getBarcode());

      assertThat(results, hasExactlyItems(barcode1, barcode2));
   }
}