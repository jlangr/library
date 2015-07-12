package domain.core.calculators;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.DateUtilTest.GROUNDHOG_DAY;
import static util.DateUtilTest.NEW_YEARS_DAY;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import util.DateUtil;
import domain.core.Book;

public class DueDateCalculatorTest {
   private DueDateCalculator calculator;

   @Before
   public void initialize() {
      calculator = new DueDateCalculator();
   }

   @Test
   public void booksDue21DaysOut() {
      assertThat(calculator.due(Book.TYPE_BOOK, NEW_YEARS_DAY),
            is(DateUtil.create(2011, Calendar.JANUARY, 22)));
      assertThat(calculator.due(Book.TYPE_BOOK, GROUNDHOG_DAY),
            is(DateUtil.create(2011, Calendar.FEBRUARY, 23)));
   }

   @Test
   public void moviesDue7DaysOut() {
      assertThat(calculator.due(Book.TYPE_MOVIE, NEW_YEARS_DAY),
            is(DateUtil.create(2011, Calendar.JANUARY, 8)));
      assertThat(calculator.due(Book.TYPE_MOVIE, GROUNDHOG_DAY),
            is(DateUtil.create(2011, Calendar.FEBRUARY, 9)));
   }

   @Test
   public void newReleasesDue7DaysOut() {
      assertThat(calculator.due(Book.TYPE_NEW_RELEASE, NEW_YEARS_DAY),
            is(DateUtil.create(2011, Calendar.JANUARY, 8)));
      assertThat(calculator.due(Book.TYPE_NEW_RELEASE, GROUNDHOG_DAY),
            is(DateUtil.create(2011, Calendar.FEBRUARY, 9)));
   }
}
