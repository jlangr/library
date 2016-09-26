package domain.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import testutil.EqualityTester;

public class BranchTest {

   @Test
   public void defaultsIdToEmpty() {
      assertThat(new Branch("alpha").getScanCode(), equalTo(""));
   }

   @Test
   public void supportsEquality() {
      String EAST_NAME = "east";
      String WEST_NAME = "west";
      String EAST_SCAN = "b111";
      String WEST_SCAN = "b222";
      Branch BRANCH_EAST = new Branch(EAST_NAME);
      Branch BRANCH_WEST = new Branch(WEST_NAME);
      Branch branch1 = BRANCH_EAST;
      branch1.setScanCode(EAST_SCAN);
      Branch branch1Copy1 = new Branch(EAST_NAME);
      branch1Copy1.setScanCode(EAST_SCAN);
      Branch branch1Copy2 = new Branch(EAST_NAME);
      branch1Copy2.setScanCode(EAST_SCAN);
      Branch branch2 = BRANCH_WEST;
      branch2.setScanCode(WEST_SCAN);
      Branch branch1Subtype = new Branch(EAST_NAME) {
      };
      branch1Subtype.setScanCode(EAST_SCAN);

      new EqualityTester(branch1, branch1Copy1, branch1Copy2, branch2, branch1Subtype).verify();
   }
}