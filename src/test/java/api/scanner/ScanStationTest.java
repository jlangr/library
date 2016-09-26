package api.scanner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import org.junit.Test;
import domain.core.*;

public class ScanStationTest extends MockedScannerSubsystemFields {
   private ScanStationState state;

   private void setScannerToMockState() {
      state = mock(ScanStationState.class);
      scanner.setCurrentState(state);
   }

   @Test
   public void initializesToWaitingState() {
      assertThat(scanner.getCurrentState(), instanceOf(ScanStationStateWaiting.class));
   }

   @Test
   public void branchIdScannedDelegatesToScanBranchId() {
      setScannerToMockState();

      scanner.scan("b123");

      verify(state).scanBranchId("b123");
   }

   @Test
   public void holdingBarcodeScannedDelegatesToScanHolding() {
      setScannerToMockState();

      scanner.scan("123:1");

      verify(state).scanHolding("123:1");
   }

   @Test
   public void patronIdScannedDelegatesToScanPatron() {
      setScannerToMockState();

      scanner.scan("p123");

      verify(state).scanPatron("p123");
   }

   @Test
   public void displaysErrorWhenInvalidBarcodeScanned() {
      scanner.scan("123");

      verify(display).showMessage(ScanStation.MSG_BAR_CODE_NOT_RECOGNIZED);
   }

   @Test
   public void inventoryIdScannedDelegatesToScanInventoryCard() {
      setScannerToMockState();
      String validInventoryId = "i" + "whatever";

      scanner.scan(validInventoryId);

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
      Patron jane = new Patron("p123", "jane");
      when(patronService.find("p123")).thenReturn(jane);

      scanner.scanPatronId("p123");

      assertThat(scanner.getPatron(), is(sameInstance(jane)));
      assertMessageDisplayed(String.format(ScanStation.MSG_PATRON_SET_TO, "jane"));
   }

   private void assertMessageDisplayed(String text) {
      verify(display).showMessage(text);
   }

   @Test
   public void setPatronIdDoesNotUpdatePatronIfNotExists() {
      when(patronService.find("p123")).thenReturn(null);

      scanner.scanPatronId("p123");

      assertThat(scanner.getPatron(), is(nullValue()));
      assertMessageDisplayed(String.format(ScanStation.MSG_NONEXISTENT_PATRON, "p123"));
   }

   @Test
   public void setBranchIdUpdatesBranchIfExists() {
      Branch branch = new Branch("b123", "West");
      when(branchService.find("b123")).thenReturn(branch);

      scanner.scanBranchId("b123");

      assertThat(scanner.getBranch(), is(sameInstance(branch)));
      assertMessageDisplayed(String.format(ScanStation.MSG_BRANCH_SET_TO, "West"));
   }

   @Test
   public void setBranchIdDoesNotUpdateBranchIfNotExists() {
      Branch westBranch = new Branch("b222", "");
      scanner.setBranch(westBranch);
      when(branchService.find("b123")).thenReturn(null);

      scanner.scanBranchId("b123");

      assertThat(scanner.getBranch(), is(sameInstance(westBranch)));
      assertMessageDisplayed(String.format(ScanStation.MSG_NONEXISTENT_BRANCH, "b123"));
   }
}