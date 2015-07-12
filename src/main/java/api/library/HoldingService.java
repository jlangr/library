package api.library;

import java.util.*;
import persistence.*;
import com.loc.material.api.*;
import domain.core.*;

public class HoldingService {
   private Catalog catalog = new Catalog();

   public void add(String holdingBarcode, String branchId) {
      throwIfHoldingAlreadyExists(holdingBarcode);
      catalog.add(create(holdingBarcode, branchId));
   }

   private Holding create(String holdingBarcode, String branchId) {
      return new Holding(createBookOrMovie(getMaterialDetails(holdingBarcode)), findBranch(branchId),
            HoldingBarcode.getCopyNumber(holdingBarcode));
   }

   private void throwIfHoldingAlreadyExists(String holdingBarcode) {
      if (find(holdingBarcode) != null)
         throw new DuplicateHoldingException();
   }

   private MaterialDetails getMaterialDetails(String holdingId) {
      MaterialDetails material = ClassificationApiFactory.getService().getMaterialDetails(
            HoldingBarcode.getClassification(holdingId));
      if (material == null)
         throw new InvalidClassificationException();
      return material;
   }

   private Branch findBranch(String branchId) {
      Branch branch = new BranchService().find(branchId);
      if (branch == null)
         throw new BranchNotFoundException("Branch not found: " + branchId);
      return branch;
   }

   private Book createBookOrMovie(MaterialDetails material) {
      return new Book(material.getAuthor(), material.getTitle(), material.getClassification(),
            material.getYear(), getType(material));
   }

   private int getType(MaterialDetails material) {
      return material.getFormat() == MaterialType.DVD ? Book.TYPE_MOVIE : Book.TYPE_BOOK;
   }

   public boolean isAvailable(String barCode) {
      Holding holding = find(barCode);
      if (holding == null)
         throw new HoldingNotFoundException();
      return holding.isAvailable();
   }

   public HoldingMap allHoldings() {
      HoldingMap stack = new HoldingMap();
      for (Holding holding: catalog)
         stack.add(holding);
      return stack;
   }

   public Holding find(String barCode) {
      return catalog.find(barCode);
   }

   public void transfer(Holding holding, Branch branch) {
      holding.transfer(branch);
   }

   public Date dateDue(String barCode) {
      Holding holding = find(barCode);
      if (holding == null) {
         throw new HoldingNotFoundException();
      }
      return holding.dateDue();
   }

   public void checkOut(String patronId, String barCode, Date date) {
      Holding holding = find(barCode);
      if (holding == null) {
         throw new HoldingNotFoundException();
      }
      if (!holding.isAvailable())
         throw new HoldingAlreadyCheckedOutException();
      holding.checkOut(date);

      PatronStore patronAccess = new PatronStore();
      Patron patron = patronAccess.find(patronId);
      patronAccess.addHoldingToPatron(patron, holding);
   }

   public int checkIn(String barCode, Date date, String branchScanCode) {
      Branch branch = new BranchService().find(branchScanCode);
      Holding hld = find(barCode);
      if (hld == null)
         throw new HoldingNotFoundException();

      // set the holding to returned status
      HoldingMap holdings = null;
      hld.checkIn(date, branch);

      // locate the patron with the checked out book
      // could introduce a patron reference ID in the holding...
      Patron f = null;
      for (Patron p: new PatronService().allPatrons()) {
         holdings = p.holdings();
         for (Holding patHld: holdings) {
            if (hld.getBook().equals(patHld.getBook()))
               f = p;
         }
      }

      // remove the book from the patron
      f.remove(hld);

      // check for late returns
      boolean isLate = false;
      Calendar c = Calendar.getInstance();
      c.setTime(hld.dateDue());
      int d = Calendar.DAY_OF_YEAR;

      // check for last day in year
      if (c.get(d) > c.getActualMaximum(d)) {
         c.set(d, 1);
         c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
      }

      if (hld.dateLastCheckedIn().after(c.getTime())) // is it late?
         isLate = true;

      if (isLate) {
         int daysLate = hld.daysLate(); // calculate # of days past due
         switch (hld.getBook().getType()) {
            case Book.TYPE_BOOK:
               f.addFine(Book.BOOK_DAILY_FINE * daysLate);
               break;
            case Book.TYPE_MOVIE:
               int fine = Math.min(1000, 100 + Book.MOVIE_DAILY_FINE * daysLate);
               f.addFine(fine);
               break;
            case Book.TYPE_NEW_RELEASE:
               f.addFine(Book.NEW_RELEASE_DAILY_FINE * daysLate);
               break;
         }
         return daysLate;
      }
      return 0;
   }
}
