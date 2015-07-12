package api.scanner;

import static api.scanner.ScanStationTestData.BRANCH_EAST;
import static api.scanner.ScanStationTestData.BRANCH_EAST_ID;
import static api.scanner.ScanStationTestData.BRANCH_WEST;
import static api.scanner.ScanStationTestData.HOLDING_ANIMAL_FARM;
import static api.scanner.ScanStationTestData.PATRON_JANE;
import static api.scanner.ScanStationTestData.PATRON_JANE_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import util.DateUtil;
import util.TimestampSource;

public class ScanStationStateReturnsTest extends ScanStationStateTest {
   @Override
   protected ScanStationState createStateObject() {
      return new ScanStationStateReturns(scanner);
   }

   @Test
   public void displaysWarningWhenCompletePressed() {
      state.pressComplete();

      assertMessageDisplayed(ScanStationStateReturns.MSG_WAITING_FOR_RETURNS);
      assertStateUnchanged();
   }

   @Test
   public void changesStateToInventoryWhenInventoryCardPressed() {
      state.scanInventoryCard();

      assertCurrentState(ScanStationStateInventory.class);
      assertMessageDisplayed(ScanStationStateReturns.MSG_INVENTORY);
   }

   @Test
   public void changesStateToCheckoutWhenPatronCardPressed() {
      when(patronService.find(PATRON_JANE_ID)).thenReturn(PATRON_JANE);
      state.scanPatron(PATRON_JANE_ID);

      assertCurrentState(ScanStationStateCheckout.class);
      assertThat(scanner.getPatronId(), is(PATRON_JANE_ID));
   }

   @Test
   public void changesBranchWhenBranchIdScanned() {
      when(branchService.find(BRANCH_EAST_ID)).thenReturn(BRANCH_EAST);
      scanner.setBranch(BRANCH_WEST);

      state.scanBranchId(BRANCH_EAST_ID);

      assertStateUnchanged();
      assertThat(scanner.getBranchId(), is(BRANCH_EAST_ID));
   }

   @Test
   public void checksInBookWhenBarcodeScanned() {
      scanner.setBranch(BRANCH_EAST);

      Date checkinDate = DateUtil.create(2011, Calendar.MARCH, 17);
      TimestampSource.queueNextTime(checkinDate);

      state.scanHolding(HOLDING_ANIMAL_FARM);

      verify(holdingService).checkIn(HOLDING_ANIMAL_FARM, checkinDate,
            BRANCH_EAST_ID);
      assertMessageDisplayed(String.format(
            ScanStationStateReturns.MSG_CHECKED_IN, HOLDING_ANIMAL_FARM));
      assertStateUnchanged();
   }
}
