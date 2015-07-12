package scratch;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import static org.hamcrest.CoreMatchers.*;

public class MultiMapTest {
   private MultiMap map;

   @Before
   public void create() {
      map = new MultiMap();
   }

   @Test
   public void isEmptyOnCreation() {
      assertTrue(map.isEmpty());
   }

   @Test
   public void isNotEmptyAfterPut() {
      map.put("key", "value");
      assertFalse(map.isEmpty());
   }

   @Test
   public void hasZeroSizeWhenCreated() {
      assertThat(map.size(), equalTo(0));
   }

   @Test
   public void incrementsSizeOnPut() {
      map.put("key", "value");
      assertThat(map.size(), equalTo(1));
   }

   @Test
   public void returnsListWithPutValue() {
      map.put("smelt", "a fish");

      assertThat(map.get("smelt"), equalTo(Arrays.asList("a fish")));
   }

   @Test
   public void returnsListWithMultiplePutValues() {
      map.put("smelt", "a fish");
      map.put("smelt", "did smell");

      assertThat(map.get("smelt"), equalTo(Arrays.asList("a fish", "did smell")));
   }

   @Test
   public void sizeRepresentsNumberOfUniqueKeys() {
      map.put("smelt", "a fish");
      map.put("dog", "a pet");

      assertThat(map.size(), equalTo(2));

   }

   @Test
   public void returnsEmptyListWhenKeyNotFound() {
      assertThat(map.get("not there"), equalTo(Collections.EMPTY_LIST));

   }

   @Test
   public void returnsListCorrespondingWithKey() {
      map.put("smelt",  "a fish");
      map.put("dog", "a pet");

      assertThat(map.get("dog"), equalTo(Arrays.asList("a pet")));
   }

   @Test(expected=IllegalArgumentException.class)
   public void throwsOnNullKey() {
      map.put(null, "");
   }

   @Test
   public void throwsWhenKeyNull() {
      try {
         map.put(null, "");
         fail();
      }
      catch (IllegalArgumentException expected) {
         assertThat(expected.getMessage(), equalTo("key is null"));
      }
   }
}

