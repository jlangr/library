package util.matchers;

import java.util.*;
import static java.util.Arrays.asList;
import org.hamcrest.*;

public class HasExactlyItemsInAnyOrder<T> extends TypeSafeMatcher<Collection<T>> {
   private Collection<T> rhsItems;

   public HasExactlyItemsInAnyOrder(Collection<T> rhsItems) {
      this.rhsItems = rhsItems;
   }

   @Override
   public void describeTo(Description description) {
      description.appendText("exactly items: " + rhsItems);
   }

   private static class Count {
      public int count = 0;
   }

   @Override
   protected boolean matchesSafely(Collection<T> lhsItems) {
      if (lhsItems == rhsItems) return true;
      if (lhsItems == null || rhsItems == null || lhsItems.size() != rhsItems.size()) return false;

      HashMap<T, Count> counts = new HashMap<>();
      count(counts, lhsItems);
      for (T item: rhsItems) {
         if (!counts.containsKey(item)) return false;
         counts.get(item).count--;
      }
      return areAllCountsZero(counts);
   }

   private boolean areAllCountsZero(HashMap<T, Count> counts) {
      for (Count count: counts.values())
         if (count.count != 0) return false;
      return true;
   }

   private void count(HashMap<T, Count> counts, Collection<T> items) {
      for (T item: items) {
         if (!counts.containsKey(item)) counts.put(item, new Count());
         counts.get(item).count++;
      }
   }

   @SafeVarargs
   @Factory
   public static <T> Matcher<Collection<T>> hasExactlyItemsInAnyOrder(T... items) {
      return new HasExactlyItemsInAnyOrder<T>(new HashSet<>(asList(items)));
   }
}
