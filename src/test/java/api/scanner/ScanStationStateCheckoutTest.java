package api.scanner;

import static api.scanner.ScanStationStateCheckout.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import org.junit.*;
import domain.core.*;
import util.*;

public class ScanStationStateCheckoutTest extends ScanStationStateTestBase {
   public static final String PATRON_JOE_ID = "p111";
   public static final Patron PATRON_JOE = new Patron(PATRON_JOE_ID, "Joe");

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
      when(holdingService.find("123:1")).thenReturn(null);

      state.scanHolding("123:1");

      assertMessageDisplayed(String.format(MSG_INVALID_HOLDING_ID, "123:1"));
      assertStateUnchanged();
   }

   @Test
   public void checksOutHoldingWhenHoldingIdScanned() {
      scanner.scanPatronId(PATRON_JOE_ID);
      when(holdingService.find("123:1")).thenReturn(holdingWithAvailability);
      TimestampSource.queueNextTime(DateUtilTest.NEW_YEARS_DAY);

      state.scanHolding("123:1");

      verify(holdingService).checkOut(PATRON_JOE_ID, "123:1", DateUtilTest.NEW_YEARS_DAY);
      assertMessageDisplayed(String.format(MSG_CHECKED_OUT, "123:1"));
      assertStateUnchanged();
   }

   @Test
   public void displaysMessageWhenBookCheckedOutTwice() {
      scanner.scanPatronId(PATRON_JOE_ID);
      when(holdingService.find("123:1")).thenReturn(holdingWithAvailability);
      state.scanHolding("123:1");
      when(holdingService.find("123:1")).thenReturn(holdingWithUnavailability);

      state.scanHolding("123:1");

      assertMessageDisplayed(String.format(ScanStationStateCheckout.MSG_ALREADY_CHECKED_OUT, "123:1"));
      assertStateUnchanged();
      assertThat(TimestampSource.isExhausted(), equalTo(true));
   }

   @Test
   public void changesStateToReturnsWhenCompletePressed() {
      scanner.scanPatronId(PATRON_JOE_ID);

      state.pressComplete();

      assertCurrentState(ScanStationStateReturns.class);
      assertMessageDisplayed(String.format(MSG_COMPLETED_CHECKOUT, PATRON_JOE_ID));
   }

   @Test
   public void displaysWarningWhenBranchIdScanned() {
      state.scanBranchId("b123");

      assertStateUnchanged();
      assertMessageDisplayed(MSG_COMPLETE_CHECKOUT_FIRST);
   }
}