package util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static util.matchers.HasExactlyItemsInAnyOrder.*;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MultiMapTest {
   private MultiMap<Object, Object> map;

   @Before
   public void initialize() {
      map = new MultiMap<Object, Object>();
   }

   @Test
   public void isEmptyOnCreation() {
      assertThat(map.size(), equalTo(0));
      assertThat(map.valuesSize(), equalTo(0));
   }

   @Test
   public void returnsValuesAssociatedWithKeyAsList() {
      map.put("a", "alpha");

      List<Object> values = map.get("a");

      assertThat(values, hasExactlyItemsInAnyOrder("alpha"));
   }

   @Test
   public void incrementsSizeForMultipleKeys() {
      map.put("a", "");
      map.put("b", "");

      assertThat(map.size(), equalTo(2));
   }

   @Test
   public void allowsMultipleElementsSameKey() {
      map.put("a", "alpha1");
      map.put("a", "alpha2");

      List<Object> values = map.get("a");

      assertThat(values, hasExactlyItemsInAnyOrder("alpha2", "alpha1"));
   }

   @Test
   public void valuesSizeRepresentsTotalCountOfValues() {
      map.put("a", "alpha");
      map.put("b", "beta1");
      map.put("b", "beta2");

      assertThat(map.valuesSize(), equalTo(3));
   }

   @Test
   public void returnsOnlyValuesAssociatedWithKey() {
      map.put("a", "alpha");
      map.put("b", "beta");

      List<Object> values = map.get("b");

      assertThat(values, hasExactlyItemsInAnyOrder("beta"));
   }

   @Test(expected = NullPointerException.class)
   public void throwsOnPutOfNullKey() {
      map.put(null, "");
   }

   @Test
   public void clearEliminatesAllData() {
      map.put("a", "");
      map.put("b", "");

      map.clear();

      assertThat(map.size(), equalTo(0));
      assertThat(map.valuesSize(), equalTo(0));
   }

   @Test
   public void returnsCombinedCollectionOfAllValues() {
      map.put("a", "alpha");
      map.put("b", "beta");

      Collection<Object> values = map.values();

      assertThat(values, hasExactlyItemsInAnyOrder("alpha", "beta"));
   }
}