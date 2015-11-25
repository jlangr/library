package api.scanner;

import static api.scanner.ScanStationStateCheckout.MSG_CHECKED_OUT;
import static api.scanner.ScanStationStateCheckout.MSG_COMPLETED_CHECKOUT;
import static api.scanner.ScanStationStateCheckout.MSG_COMPLETE_CHECKOUT_FIRST;
import static api.scanner.ScanStationStateCheckout.MSG_INVALID_HOLDING_ID;
import static api.scanner.ScanStationTestData.BRANCH_WEST_ID;
import static api.scanner.ScanStationTestData.HOLDING_ANIMAL_FARM;
import static api.scanner.ScanStationTestData.PATRON_JOE;
import static api.scanner.ScanStationTestData.PATRON_JOE_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import util.DateUtilTest;
import util.TimestampSource;
import domain.core.Holding;

public class ScanStationStateCheckoutTest extends ScanStationStateTestBase {
   private Holding holdingWithAvailability;
   private Holding holdingWithUnavailability;

   @Override
   protected ScanStationState createStateObject() {
      return new ScanStationStateCheckout(scanner);
   }

   @Before
   public void initialize() {
      holdingWithAvailability = createHoldingWithAvailability(true);
      holdingWithUnavailability = createHoldingWithAvailability(false);
   }

   private Holding createHoldingWithAvailability(boolean isAvailable) {
      Holding holding = mock(Holding.class);
      when(holding.isAvailable()).thenReturn(isAvailable);
      return holding;
   }

   @Before
   public void addPatronJoe() {
      when(patronService.find(PATRON_JOE_ID)).thenReturn(PATRON_JOE);
   }

   @Test
   public void displaysWarningWhenPatronCardScanned() {
      state.scanPatron(PATRON_JOE_ID);

      assertMessageDisplayed(MSG_COMPLETE_CHECKOUT_FIRST);
      assertStateUnchanged();
   }

   @Test
   public void displaysWarningWhenInventoryCardScanned() {
      state.scanInventoryCard();

      assertMessageDisplayed(MSG_COMPLETE_CHECKOUT_FIRST);
      assertStateUnchanged();
   }

   @Test
   public void displaysMessageIfNoHoldingExists() {
      scanner.scanPatronId(PATRON_JOE_ID);
      when(holdingService.find(HOLDING_ANIMAL_FARM)).thenReturn(null);

      state.scanHolding(HOLDING_ANIMAL_FARM);

      assertMessageDisplayed(String.format(MSG_INVALID_HOLDING_ID,
            HOLDING_ANIMAL_FARM));
      assertStateUnchanged();
   }

   @Test
   public void checksOutHoldingWhenHoldingIdScanned() {
      scanner.scanPatronId(PATRON_JOE_ID);
      when(holdingService.find(HOLDING_ANIMAL_FARM)).thenReturn(
            holdingWithAvailability);
      TimestampSource.queueNextTime(DateUtilTest.NEW_YEARS_DAY);

      state.scanHolding(HOLDING_ANIMAL_FARM);

      verify(holdingService).checkOut(PATRON_JOE_ID, HOLDING_ANIMAL_FARM,
            DateUtilTest.NEW_YEARS_DAY);
      assertMessageDisplayed(String
            .format(MSG_CHECKED_OUT, HOLDING_ANIMAL_FARM));
      assertStateUnchanged();
   }

   @Test
   public void displaysMessageWhenBookCheckedOutTwice() {
      scanner.scanPatronId(PATRON_JOE_ID);
      when(holdingService.find(HOLDING_ANIMAL_FARM)).thenReturn(
            holdingWithAvailability);
      state.scanHolding(HOLDING_ANIMAL_FARM);
      when(holdingService.find(HOLDING_ANIMAL_FARM)).thenReturn(
            holdingWithUnavailability);

      state.scanHolding(HOLDING_ANIMAL_FARM);

      assertMessageDisplayed(String.format(
            ScanStationStateCheckout.MSG_ALREADY_CHECKED_OUT,
            HOLDING_ANIMAL_FARM));
      assertStateUnchanged();
      assertThat(TimestampSource.isExhausted(), is(true));
   }

   @Test
   public void changesStateToReturnsWhenCompletePressed() {
      scanner.scanPatronId(PATRON_JOE_ID);

      state.pressComplete();

      assertCurrentState(ScanStationStateReturns.class);
      assertMessageDisplayed(String.format(MSG_COMPLETED_CHECKOUT,
            PATRON_JOE_ID));
   }

   @Test
   public void displaysWarningWhenBranchIdScanned() {
      state.scanBranchId(BRANCH_WEST_ID);

      assertStateUnchanged();
      assertMessageDisplayed(MSG_COMPLETE_CHECKOUT_FIRST);
   }
}
