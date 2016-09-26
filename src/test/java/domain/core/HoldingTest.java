package domain.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;
import com.loc.material.api.*;
import testutil.*;
import util.*;

/*
This test class is a mess. Some of the opportunities for cleanup:

 - AAA used but no visual separation
 - seeming use of AAA but it's not really
 - unnecessary code
 - odd constants
 - can data be created in the test?
 - poor / inconsistent test names
 - comments in tests (are they even true)?
 - multiple behaviors/asserts per test
 - code in the wrong place / opportunities for reuse of existing code
 */

public class HoldingTest {
   private static final MaterialDetails THE_TRIAL = new MaterialDetails("10", "", "10", "", "");
   private static final MaterialDetails DR_STRANGELOVE = new MaterialDetails("12", "", "11", "", MaterialType.DVD, "");
   private static final MaterialDetails THE_REVENANT = new MaterialDetails("12", "", "11", "", MaterialType.NewReleaseDVD, "");
   private Holding h;
   private static final Date TODAY = new Date();
   private static final int COPY_NUMBER_1 = 1;
   private Branch eastBranch = new Branch("East");
   private Branch westBranch = new Branch("West");

   @Before
   public void setUp() {
      h = new Holding(THE_TRIAL, eastBranch, COPY_NUMBER_1);
   }

   @Test
   public void branchDefaultsToCheckedOutWhenCreated() {
      Holding holding = new Holding(THE_TRIAL);
      assertThat(holding.getBranch(), equalTo(Branch.CHECKED_OUT));
   }

   @Test
   public void copyNumberDefaultsTo1WhenCreated() {
      Holding holding = new Holding(THE_TRIAL, eastBranch);
      assertThat(holding.getCopyNumber(), equalTo(1));
   }

   @Test
   public void changesBranchOnTransfer() {
      h.transfer(westBranch);
      assertThat(h.getBranch(), equalTo(westBranch));
   }

   @Test
   public void ck() {
      h.checkOut(TODAY);
      assertThat(h.dateCheckedOut(), equalTo(TODAY));
      assertTrue(h.dateDue().after(TODAY));
      assertThat(h.getBranch(), equalTo(Branch.CHECKED_OUT));
      assertFalse(h.isAvailable());

      h.checkOut(TODAY);
      Date tomorrow = new Date(TODAY.getTime() + 60L + 60 * 1000 * 24);
      h.checkIn(tomorrow, eastBranch);
      assertThat(h.dateLastCheckedIn(), equalTo(tomorrow));
      assertTrue(h.isAvailable());
      assertThat(h.getBranch(), equalTo(eastBranch));
   }

   @Test
   public void returnDateForStandardBook() {
      h.checkOut(TODAY);
      Date dateDue = h.dateDue();
      assertDateEquals(addDays(TODAY, MaterialType.Book.getCheckoutPeriod()), dateDue);
   }

   @Test
   public void testSomething() {
      // movie
      checkOutToday(DR_STRANGELOVE, eastBranch);
      Date expected = addDays(TODAY, MaterialType.DVD.getCheckoutPeriod());
      assertDateEquals(addDays(TODAY, MaterialType.DVD.getCheckoutPeriod()), h.dateDue());

      // childrens movie
      checkOutToday(THE_REVENANT, eastBranch);
      expected = addDays(TODAY, MaterialType.NewReleaseDVD.getCheckoutPeriod());
      assertDateEquals(expected, h.dateDue());
   }

   @Test
   public void answersDaysLateOfZeroWhenReturnedSameDay() {
      checkOutToday(THE_TRIAL, eastBranch);
      int daysLate = h.checkIn(TODAY, eastBranch);
      assertThat(daysLate, equalTo(0));
   }

   @Test
   public void answersDaysLateOfZeroWhenReturnedOnDateDue() {
      checkOutToday(THE_TRIAL, eastBranch);
      int daysLate = h.checkIn(h.dateDue(), eastBranch);
      assertThat(daysLate, equalTo(0));
   }

   @Test
   public void answersDaysLateWhenReturnedAfterDueDate() {
      checkOutToday(THE_TRIAL, eastBranch);
      Date date = DateUtil.addDays(h.dateDue(), 3);
      int days = h.checkIn(date, eastBranch);
      assertThat(days, equalTo(3));
   }

   private void checkOutToday(MaterialDetails material, Branch branch) {
      h = new Holding(material, branch);
      h.checkOut(TODAY);
   }

   static void assertMaterial(MaterialDetails expected, Holding holding) {
      MaterialTestUtil.assertMaterialsEqual(expected, holding.getMaterial());
   }

   public static Date addDays(Date date, int days) {
      return new Date(date.getTime() + days * 60L * 1000 * 60 * 24);
   }

   public static void assertDateEquals(Date expectedDate, Date actualDate) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(expectedDate);
      int expectedYear = calendar.get(Calendar.YEAR);
      int expectedDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
      calendar.setTime(actualDate);
      assertThat(calendar.get(Calendar.YEAR), equalTo(expectedYear));
      assertThat(calendar.get(Calendar.DAY_OF_YEAR), equalTo(expectedDayOfYear));
   }

   @Test
   public void equality() {
      Holding holding1 = new Holding(THE_TRIAL, eastBranch, 1);
      Holding holding1Copy1 = new Holding(THE_TRIAL, westBranch, 1); // diff loc but same copy
      Holding holding1Copy2 = new Holding(THE_TRIAL, Branch.CHECKED_OUT, 1);
      Holding holding2 = new Holding(THE_TRIAL, eastBranch, 2); // 2nd copy
      Holding holding1Subtype = new Holding(THE_TRIAL, eastBranch,
            1) {
      };

      new EqualityTester(holding1, holding1Copy1, holding1Copy2, holding2,
            holding1Subtype).verify();
   }
}