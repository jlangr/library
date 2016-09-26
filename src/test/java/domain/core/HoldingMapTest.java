package domain.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static util.matchers.HasExactlyItemsInAnyOrder.hasExactlyItemsInAnyOrder;
import java.util.*;
import org.junit.*;

public class HoldingMapTest {
   private HoldingMap map;
   private static final Holding THE_TRIAL_HOLDING = new Holding(MaterialTestData.THE_TRIAL);
   private static final Holding AGILE_JAVA_HOLDING = new Holding(MaterialTestData.AGILE_JAVA);

   @Before
   public void initialize() {
      map = new HoldingMap();
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
      assertFalse(map.contains(THE_TRIAL_HOLDING));
   }

   @Test
   public void containsAddedHolding() {
      map.add(THE_TRIAL_HOLDING);

      assertTrue(map.contains(THE_TRIAL_HOLDING));
   }

   @Test
   public void sizeIncrementedOnAddingHolding() {
      map.add(THE_TRIAL_HOLDING);

      assertThat(map.size(), equalTo(1));
   }

   @Test
   public void retrievesHoldingByBarcode() {
      map.add(THE_TRIAL_HOLDING);

      Holding retrieved = map.get(THE_TRIAL_HOLDING.getBarCode());

      assertSame(retrieved, THE_TRIAL_HOLDING);
   }

   @Test
   public void returnsAllHoldings() {
      map.add(THE_TRIAL_HOLDING);
      map.add(AGILE_JAVA_HOLDING);

      Collection<Holding> holdings = map.holdings();

      assertThat(holdings, hasExactlyItemsInAnyOrder(THE_TRIAL_HOLDING, AGILE_JAVA_HOLDING));
   }

   @Test
   public void removeHolding() {
      map.add(THE_TRIAL_HOLDING);

      map.remove(THE_TRIAL_HOLDING);

      assertFalse(map.contains(THE_TRIAL_HOLDING));
   }

   @Test
   public void removeHoldingDecrementsSize() {
      map.add(THE_TRIAL_HOLDING);

      map.remove(THE_TRIAL_HOLDING);

      assertThat(map.size(), equalTo(0));
   }

   @Test
   public void supportsIteration() {
      map.add(THE_TRIAL_HOLDING);
      map.add(AGILE_JAVA_HOLDING);

      Collection<Holding> retrieved = new ArrayList<Holding>();
      for (Holding holding: map)
         retrieved.add(holding);

      assertThat(retrieved, hasExactlyItemsInAnyOrder(THE_TRIAL_HOLDING, AGILE_JAVA_HOLDING));
   }
}
