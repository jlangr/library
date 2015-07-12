package api.scanner;

import api.library.BranchService;
import api.library.HoldingService;
import api.library.PatronService;

import com.nssi.devices.model1801c.ScanDisplayListener;
import com.nssi.devices.model1801c.ScanListener;

import domain.core.Branch;
import domain.core.Patron;

public class ScanStation implements ScanListener {
   public static final String MSG_NONEXISTENT_BRANCH = "Nonexistent branch id %s";
   public static final String MSG_NONEXISTENT_PATRON = "Nonexistent patron id %s";
   public static final String MSG_BRANCH_SET_TO = "Branch now set to %s";
   public static final String MSG_PATRON_SET_TO = "Patron now set to %s";
   public static final String MSG_BAR_CODE_NOT_RECOGNIZED = "Bar code not recognized.";

   private HoldingService librarySystem = new HoldingService();
   private PatronService patronService = new PatronService();
   private BranchService branchService = new BranchService();
   private ScanStationState state = new ScanStationStateWaiting(this);
   private ScanDisplayListener display;
   private Branch branch = Branch.CHECKED_OUT;
   private Patron patron;

   public ScanStation(ScanDisplayListener display) {
      this.display = display;
   }

   @Override
   public void scan(String barcode) {
      switch (BarcodeInterpreter.typeOf(barcode)) {
         case Branch:
            state.scanBranchId(barcode);
            break;
         case Inventory:
            state.scanInventoryCard();
            break;
         case Holding:
            state.scanHolding(barcode);
            break;
         case Patron:
            state.scanPatron(barcode);
            break;
         case Unrecognized:
            showMessage(MSG_BAR_CODE_NOT_RECOGNIZED);
            break;
      }
   }

   @Override
   public void pressComplete() {
      state.pressComplete();
   }

   public ScanStationState getCurrentState() {
      return state;
   }

   public void setCurrentState(ScanStationState state) {
      this.state = state;
   }

   public void setLibrarySystem(HoldingService librarySystem) {
      this.librarySystem = librarySystem;
   }

   public HoldingService getLibrarySystem() {
      return librarySystem;
   }

   public void setPatronService(PatronService patronService) {
      this.patronService = patronService;
   }

   public PatronService getPatronService() {
      return patronService;
   }

   public void setBranchService(BranchService branchService) {
      this.branchService = branchService;
   }

   public BranchService getBranchService() {
      return branchService;
   }

   public void showMessage(String text) {
      display.showMessage(text);
   }

   public void scanPatronId(String patronId) {
      Patron retrievedPatron = patronService.find(patronId);
      if (retrievedPatron == null) {
         showMessage(String
               .format(ScanStation.MSG_NONEXISTENT_PATRON, patronId));
         return;
      }
      this.patron = retrievedPatron;
      showMessage(String
            .format(ScanStation.MSG_PATRON_SET_TO, patron.getName()));
   }

   public String getPatronId() {
      if (patron == null)
         return null;
      return patron.getId();
   }

   public Patron getPatron() {
      return patron;
   }

   void setPatron(Patron patron) {
      this.patron = patron;
   }

   public void setBranch(Branch branch) {
      this.branch = branch;
   }

   public String getBranchId() {
      return branch.getScanCode();
   }

   public void scanBranchId(String branchId) {
      Branch branch = branchService.find(branchId);
      if (branch == null) {
         showMessage(String
               .format(ScanStation.MSG_NONEXISTENT_BRANCH, branchId));
         return;
      }
      this.branch = branch;
      showMessage(String
            .format(ScanStation.MSG_BRANCH_SET_TO, branch.getName()));
   }

   public Branch getBranch() {
      return branch;
   }
}
