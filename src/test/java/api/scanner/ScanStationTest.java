package api.scanner;

import static api.scanner.ScanStationTestData.BRANCH_EAST;
import static api.scanner.ScanStationTestData.BRANCH_EAST_ID;
import static api.scanner.ScanStationTestData.BRANCH_WEST;
import static api.scanner.ScanStationTestData.HOLDING_ANIMAL_FARM;
import static api.scanner.ScanStationTestData.INVENTORY_ID;
import static api.scanner.ScanStationTestData.PATRON_JANE;
import static api.scanner.ScanStationTestData.PATRON_JANE_ID;
import static api.scanner.ScanStationTestData.PATRON_JOE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ScanStationTest extends MockedScannerSubsystemFields {
   private ScanStationState state;

   private void setScannerToMockState() {
      state = mock(ScanStationState.class);
      scanner.setCurrentState(state);
   }

   @Test
   public void initializesToWaitingState() {
      assertThat(scanner.getCurrentState(),
            instanceOf(ScanStationStateWaiting.class));
   }

   @Test
   public void branchIdScannedDelegatesToScanBranchId() {
      setScannerToMockState();

      scanner.scan(BRANCH_EAST_ID);

      verify(state).scanBranchId(BRANCH_EAST_ID);
   }

   @Test
   public void holdingBarcodeScannedDelegatesToScanHolding() {
      setScannerToMockState();

      scanner.scan(HOLDING_ANIMAL_FARM);

      verify(state).scanHolding(HOLDING_ANIMAL_FARM);
   }

   @Test
   public void patronIdScannedDelegatesToScanPatron() {
      setScannerToMockState();

      scanner.scan(PATRON_JANE_ID);

      verify(state).scanPatron(PATRON_JANE_ID);
   }
   
   
   @Test
   public void displaysErrorWhenInvalidBarcodeScanned() {
      scanner.scan("123");
      
      verify(display).showMessage(ScanStation.MSG_BAR_CODE_NOT_RECOGNIZED);
   }

   @Test
   public void inventoryIdScannedDelegatesToScanInventoryCard() {
      setScannerToMockState();

      scanner.scan(INVENTORY_ID);

      verify(state).scanInventoryCard();
   }

   @Test
   public void pressCompleteDelegatesToState() {
      setScannerToMockState();

      scanner.pressComplete();

      verify(state).pressComplete();
   }

   @Test
   public void showMessageDelegatesToListener() {
      scanner.showMessage("Hey");

      verify(display).showMessage("Hey");
   }

   @Test
   public void setPatronIdUpdatesPatronIfExists() {
      scanner.setPatron(PATRON_JOE);
      when(patronService.find(PATRON_JANE_ID)).thenReturn(PATRON_JANE);

      scanner.scanPatronId(PATRON_JANE_ID);

      assertThat(scanner.getPatron(), is(sameInstance(PATRON_JANE)));
      assertMessageDisplayed(String.format(ScanStation.MSG_PATRON_SET_TO,
            PATRON_JANE.getName()));
   }

   private void assertMessageDisplayed(String text) {
      verify(display).showMessage(text);
   }

   @Test
   public void setPatronIdDoesNotUpdatePatronIfNotExists() {
      scanner.setPatron(PATRON_JOE);
      when(patronService.find(PATRON_JANE_ID)).thenReturn(null);

      scanner.scanPatronId(PATRON_JANE_ID);

      assertThat(scanner.getPatron(), is(sameInstance(PATRON_JOE)));
      assertMessageDisplayed(String.format(ScanStation.MSG_NONEXISTENT_PATRON,
            PATRON_JANE_ID));
   }

   @Test
   public void setBranchIdUpdatesBranchIfExists() {
      scanner.setBranch(BRANCH_WEST);
      when(branchService.find(BRANCH_EAST_ID)).thenReturn(BRANCH_EAST);

      scanner.scanBranchId(BRANCH_EAST_ID);

      assertThat(scanner.getBranch(), is(sameInstance(BRANCH_EAST)));
      assertMessageDisplayed(String.format(ScanStation.MSG_BRANCH_SET_TO,
            BRANCH_EAST.getName()));
   }

   @Test
   public void setBranchIdDoesNotUpdateBranchIfNotExists() {
      scanner.setBranch(BRANCH_WEST);
      when(branchService.find(BRANCH_EAST_ID)).thenReturn(null);

      scanner.scanBranchId(BRANCH_EAST_ID);

      assertThat(scanner.getBranch(), is(sameInstance(BRANCH_WEST)));
      assertMessageDisplayed(String.format(ScanStation.MSG_NONEXISTENT_BRANCH,
            BRANCH_EAST_ID));
   }
}
