package api.scanner;

import domain.core.Branch;
import domain.core.Patron;

public class ScanStationTestData {
   public static final String BRANCH_EAST_ID = "b234";
   public static final String BRANCH_WEST_ID = "b123";
   public static final Branch BRANCH_EAST = Branch.createTest("East", BRANCH_EAST_ID);
   public static final Branch BRANCH_WEST = Branch.createTest("West", BRANCH_WEST_ID);

   public static final String PATRON_JOE_ID = "p111";
   public static final String PATRON_JANE_ID = "p222";

   public static final Patron PATRON_JOE = new Patron(PATRON_JOE_ID, "Joe");
   public static final Patron PATRON_JANE = new Patron(PATRON_JANE_ID, "Jane");

   public static final String HOLDING_ANIMAL_FARM = "123:1";
   public static final String HOLDING_CATCH22_COPY_1 = "234:1";
   public static final String HOLDING_CATCH22_COPY_2 = "234:2";

   public static final String INVENTORY_ID = "i999";
}
