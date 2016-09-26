package util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static util.matchers.LessThan.lessThan;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Test;

public class TimestampSourceTest {
   static final Date NEW_YEARS_DAY = DateUtil.create(2011, Calendar.JANUARY, 1);

   @After
   public void clearTimestampSource() {
      TimestampSource.emptyQueue();
   }

   @Test
   public void retrievesSinglePushedTime() {
      TimestampSource.queueNextTime(NEW_YEARS_DAY);

      assertThat(TimestampSource.now(), equalTo(NEW_YEARS_DAY));
   }

   @Test
   public void retrievesMultiplePushedTimes() {
      Date groundhogDay = DateUtil.create(2011, Calendar.FEBRUARY, 2);
      TimestampSource.queueNextTime(NEW_YEARS_DAY);
      TimestampSource.queueNextTime(groundhogDay);

      assertThat(TimestampSource.now(), equalTo(NEW_YEARS_DAY));
      assertThat(TimestampSource.now(), equalTo(groundhogDay));
   }

   @Test
   public void isNotExhaustedWhenTimeQueued() {
      TimestampSource.queueNextTime(NEW_YEARS_DAY);
      assertThat(TimestampSource.isExhausted(), equalTo(false));
   }

   @Test
   public void isExhaustedWhenNoTimeQueued() {
      assertThat(TimestampSource.isExhausted(), equalTo(true));
      TimestampSource.queueNextTime(NEW_YEARS_DAY);
      TimestampSource.now();
      assertThat(TimestampSource.isExhausted(), equalTo(true));
   }

   @Test
   public void returnsCurrentTimeWhenQueueExhausted() {
      TimestampSource.queueNextTime(NEW_YEARS_DAY);

      Date now = new Date();
      TimestampSource.now();
      Date retrievedNow = TimestampSource.now();
      assertThat(retrievedNow.getTime() - now.getTime(), lessThan(100));
   }
}
