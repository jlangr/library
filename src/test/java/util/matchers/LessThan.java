package util.matchers;

// The full package of Hamcrest includes these
// comparisons. Implemented here for demo purposes
// (and avoid pulling in Hamcrest library).

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class LessThan extends TypeSafeMatcher<Number> {
   private Number rhs;

   public LessThan(Number rhs) {
      this.rhs = rhs;
   }

   @Override
   public void describeTo(Description description) {
      description.appendText("A number less than " + rhs);
   }

   @Override
   public boolean matchesSafely(Number lhs) {
      return lhs.doubleValue() < rhs.doubleValue();
   }

   @Factory
   public static <T> Matcher<Number> lessThan(Number number) {
      return new LessThan(number);
   }
}
