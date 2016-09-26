package domain.core;

public class Branch {
   public static final Branch CHECKED_OUT = new Branch("b999999", "unavailable");
   private String name;
   private String scanCode;

   public Branch(String name) {
      this("", name);
   }

   public Branch(String scanCode, String name) {
      this.name = name;
      this.scanCode = scanCode;
   }

   public String getScanCode() {
      return scanCode;
   }

   public void setScanCode(String scanCode) {
      this.scanCode = scanCode;
   }

   public String getName() {
      return name;
   }

   @Override
   public boolean equals(Object object) {
      if (object == null)
         return false;
      if ((object.getClass() != this.getClass()))
         return false;
      Branch that = (Branch)object;
      return this.getScanCode().equals(that.getScanCode());
   }

   @Override
   public int hashCode() {
      return scanCode.hashCode();
   }

   @Override
   public String toString() {
      return "[Branch] " + name;
   }

   public static Branch createTest(String name, String scanCode) {
      return new Branch(scanCode, name);
   }
}