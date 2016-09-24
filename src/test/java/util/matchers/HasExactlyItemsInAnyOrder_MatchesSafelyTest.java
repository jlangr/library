package util.matchers;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import static java.util.Arrays.asList;

public class HasExactlyItemsInAnyOrder_MatchesSafelyTest {
   @Test
   public void falseWhenArgIsNull() {
      assertFalse(createMatcher(asList("a")).matchesSafely(null));
   }

   @Test
   public void falseWhenReceiverListIsNull() {
      assertFalse(createMatcher(null).matchesSafely(asList("a")));
   }

   @Test
   public void trueWhenBothAreNull() {
      assertTrue(createMatcher(null).matchesSafely(null));
   }

   @Test
   public void falseWhenSizesDiffer() {
      assertFalse(createMatcher(asList("a")).matchesSafely(asList("a", "b")));
   }

   @Test
   public void trueWhenBothContainSingleSameItem() {
      assertTrue(createMatcher(asList("a")).matchesSafely(asList("a")));
   }

   @Test
   public void trueWhenBothContainSameItemsInSameOrder() {
      assertTrue(createMatcher(asList("a", "b")).matchesSafely(asList("a", "b")));
   }

   @Test
   public void trueWhenBothContainSameItemsDifferentOrder() {
      assertTrue(createMatcher(asList("a", "b")).matchesSafely(asList("b", "a")));
   }

   @Test
   public void falseWhenBothHaveDifferingDuplicatesButSetsMatch() {
      assertFalse(createMatcher(asList("a", "b", "b")).matchesSafely(asList("b", "a", "a")));
   }

   @Test
   public void falseWhenOneIsSubsetOfOther() {
      assertFalse(createMatcher(asList("a", "b", "b")).matchesSafely(asList("b", "a", "c")));
   }

   @Test
   public void falseWhenItemsDontMatch() {
      assertFalse(createMatcher(asList("a", "b", "c")).matchesSafely(asList("a", "b", "d")));
   }

   private HasExactlyItemsInAnyOrder<String> createMatcher(List<String> arg) {
      return new HasExactlyItemsInAnyOrder<String>(arg);
   }
}