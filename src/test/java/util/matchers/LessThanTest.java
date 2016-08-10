package util.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static util.matchers.LessThan.lessThan;

import org.junit.Test;

public class LessThanTest {
   @Test
   public void passesWhenLessThan() {
      assertThat(5, lessThan(6));
   }

   @Test
   public void notPassesWhenEqualTo() {
      assertThat(6, not(lessThan(6)));
   }

   @Test
   public void notPassesWhenGreaterThan() {
      assertThat(7, not(lessThan(6)));
   }

   @Test
   public void passesWithDoubles() {
      assertThat(5.0, lessThan(6.0));
   }

   @Test
   public void failureMessageIsUseful() {
//      try {
//         assertThat(6, lessThan(4));
//      } catch (AssertionError e) {
//    //     assertThat(e.getMessage(), containsString("A number less than 4"));
//         // TODO
//      }
   }
}
