package domain.core;

import java.util.Date;
import com.loc.material.api.*;
import util.*;

public class Holding {
   public static final String BARCODE_SEPARATOR = ":";
   private MaterialDetails material;
   private Branch branch;
   private Date dateCheckedOut;
   private Date dateLastCheckedIn;
   private int copyNumber;

   public Holding(MaterialDetails material) {
      this(material, Branch.CHECKED_OUT);
   }

   public Holding(MaterialDetails material, Branch branch) {
      this(material, branch, 1);
   }

   public Holding(MaterialDetails material, Branch branch, int copyNumber) {
      this.material = material;
      this.branch = branch;
      this.copyNumber = copyNumber;
   }

   public MaterialDetails getMaterial() {
      return material;
   }

   public Branch getBranch() {
      return branch;
   }

   public int getCopyNumber() {
      return copyNumber;
   }

   public String getBarcode() {
      return HoldingBarcode.createCode(material.getClassification(), copyNumber);
   }

   public void setCopyNumber(int copyNumber) {
      this.copyNumber = copyNumber;
   }

   public void transfer(Branch newBranch) {
      branch = newBranch;
   }

   public boolean isAvailable() {
      return branch != Branch.CHECKED_OUT;
   }

   public void checkOut(Date date) {
      branch = Branch.CHECKED_OUT;
      dateCheckedOut = date;
   }

   public Date dateCheckedOut() {
      return dateCheckedOut;
   }

   public Date dateDue() {
      return DateUtil.addDays(dateCheckedOut, getHoldingPeriod());
   }

   private int getHoldingPeriod() {
      return material.getFormat().getCheckoutPeriod();
   }

   public int checkIn(Date date, Branch branch) {
      this.dateLastCheckedIn = date;
      this.branch = branch;
      return daysLate();
   }

   public int daysLate() {
      return DateUtil.daysAfter(dateDue(), dateLastCheckedIn());
   }

   public Date dateLastCheckedIn() {
      return dateLastCheckedIn;
   }

   @Override
   public int hashCode() {
      return getBarcode().hashCode();
   }

   @Override
   public boolean equals(Object object) {
      if (object == null)
         return false;
      if (this.getClass() != object.getClass())
         return false;
      Holding that = (Holding)object;
      return this.getBarcode().equals(that.getBarcode());
   }

   @Override
   public String toString() {
      return material.toString() + "(" + copyNumber + ") @ " + branch.getName();
   }
}