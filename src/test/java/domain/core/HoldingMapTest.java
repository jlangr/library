package domain.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static util.matchers.HasExactlyItemsInAnyOrder.hasExactlyItemsInAnyOrder;
import java.util.*;
import org.junit.*;

public class HoldingMapTest {
   private HoldingMap map;
   private Holding holding;

   @Before
   public void initialize() {
      map = new HoldingMap();
      holding = new HoldingBuilder().create();
   }

   @Test
   public void isEmptyWhenCreated() {
      assertTrue(map.isEmpty());
   }

   @Test
   public void hasSizeZeroWhenCreated() {
      assertThat(map.size(), equalTo(0));
   }

   @Test
   public void containsFailsWhenHoldingNotFound() {
      assertFalse(map.contains(holding));
   }

   @Test
   public void containsAddedHolding() {
      map.add(holding);

      assertTrue(map.contains(holding));
   }

   @Test
   public void sizeIncrementedOnAddingHolding() {
      map.add(holding);

      assertThat(map.size(), equalTo(1));
   }

   @Test
   public void retrievesHoldingByBarcode() {
      map.add(holding);

      Holding retrieved = map.get(holding.getBarcode());

      assertSame(retrieved, holding);
   }

   @Test
   public void returnsAllHoldings() {
      Holding holdingA = new HoldingBuilder().withClassification("a").create();
      Holding holdingB = new HoldingBuilder().withClassification("b").create();
      map.add(holdingA);
      map.add(holdingB);

      Collection<Holding> holdings = map.holdings();

      assertThat(holdings, hasExactlyItemsInAnyOrder(holdingA, holdingB));
   }

   @Test
   public void removeHolding() {
      map.add(holding);

      map.remove(holding);

      assertFalse(map.contains(holding));
   }

   @Test
   public void removeHoldingDecrementsSize() {
      map.add(holding);

      map.remove(holding);

      assertThat(map.size(), equalTo(0));
   }

   @Test
   public void supportsIteration() {
      Holding holdingA = new HoldingBuilder().withClassification("a").create();
      Holding holdingB = new HoldingBuilder().withClassification("b").create();
      map.add(holdingA);
      map.add(holdingB);

      Collection<Holding> retrieved = new ArrayList<Holding>();
      for (Holding holding: map)
         retrieved.add(holding);

      assertThat(retrieved, hasExactlyItemsInAnyOrder(holdingA, holdingB));
   }
}