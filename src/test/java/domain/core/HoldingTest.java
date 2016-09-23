package domain.core;

import static domain.core.MaterialTestData.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;
import com.loc.material.api.*;
import testutil.*;
import util.*;

public class HoldingTest {
   private Holding holding;
   private static final Date TODAY = new Date();
   private static final int COPY_NUMBER_1 = 1;
   private static final Branch EAST_BRANCH = new Branch("East");

   @Before
   public void setUp() {
      holding = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST, COPY_NUMBER_1);
   }

   @Test
   public void branchDefaultsToCheckedOutWhenCreated() {
      Holding holding = new Holding(THE_TRIAL);

      assertThat(holding.getBranch(), equalTo(Branch.CHECKED_OUT));
   }

   @Test
   public void copyNumberDefaultsTo1WhenCreated() {
      Holding holding = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST);

      assertThat(holding.getCopyNumber(), equalTo(1));
   }

   @Test
   public void changesBranchOnTransfer() {
      holding.transfer(BranchTest.BRANCH_WEST);

      assertThat(holding.getBranch(), equalTo(BranchTest.BRANCH_WEST));
   }

   @Test
   public void ck() {
      holding.checkOut(TODAY);
      assertThat(holding.dateCheckedOut(), equalTo(TODAY));
      assertTrue(holding.dateDue().after(TODAY));
      assertThat(holding.getBranch(), equalTo(Branch.CHECKED_OUT));
      assertFalse(holding.isAvailable());

      holding.checkOut(TODAY);
      Date tomorrow = new Date(TODAY.getTime() + 60L + 60 * 1000 * 24);
      holding.checkIn(tomorrow, EAST_BRANCH);
      assertThat(holding.dateLastCheckedIn(), equalTo(tomorrow));
      assertTrue(holding.isAvailable());
      assertThat(holding.getBranch(), equalTo(EAST_BRANCH));
   }

   @Test
   public void returnDateForStandardBook() {
      holding.checkOut(TODAY);
      Date dateDue = holding.dateDue();
      assertDateEquals(addDays(TODAY, MaterialType.Book.getCheckoutPeriod()), dateDue);
   }

   @Test
   public void testSomething() {
      // movie
      checkOutToday(DR_STRANGELOVE, BranchTest.BRANCH_EAST);
      Date expected = addDays(TODAY, MaterialType.DVD.getCheckoutPeriod());
      assertDateEquals(addDays(TODAY, MaterialType.DVD.getCheckoutPeriod()), holding.dateDue());

      // childrens movie
      checkOutToday(THE_TRIAL_NEW_EDITION, BranchTest.BRANCH_EAST);
      expected = addDays(TODAY, MaterialType.Book.getCheckoutPeriod());
      assertDateEquals(expected, holding.dateDue());
   }

   @Test
   public void answersDaysLateOfZeroWhenReturnedSameDay() {
      checkOutToday(THE_TRIAL, BranchTest.BRANCH_EAST);

      int daysLate = holding.checkIn(TODAY, BranchTest.BRANCH_EAST);

      assertThat(daysLate, equalTo(0));
   }

   @Test
   public void answersDaysLateOfZeroWhenReturnedOnDateDue() {
      checkOutToday(THE_TRIAL, BranchTest.BRANCH_EAST);

      int daysLate = holding.checkIn(holding.dateDue(), BranchTest.BRANCH_EAST);

      assertThat(daysLate, equalTo(0));
   }

   @Test
   public void answersDaysLateWhenReturnedAfterDueDate() {
      checkOutToday(THE_TRIAL, BranchTest.BRANCH_EAST);
      Date threeDaysLate = DateUtil.addDays(holding.dateDue(), 3);

      int daysLate = holding.checkIn(threeDaysLate, BranchTest.BRANCH_EAST);

      assertThat(daysLate, equalTo(3));
   }

   private void checkOutToday(MaterialDetails material, Branch branch) {
      holding = new Holding(material, branch);
      holding.checkOut(TODAY);
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
      Holding holding1 = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST, 1);
      Holding holding1Copy1 = new Holding(THE_TRIAL, BranchTest.BRANCH_WEST, 1); // diff loc but same copy
      Holding holding1Copy2 = new Holding(THE_TRIAL, Branch.CHECKED_OUT, 1);
      Holding holding2 = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST, 2); // 2nd copy
      Holding holding1Subtype = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST,
            1) {
      };

      new EqualityTester(holding1, holding1Copy1, holding1Copy2, holding2,
            holding1Subtype).verify();
   }
}