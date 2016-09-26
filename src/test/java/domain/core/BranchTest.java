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
      Branch branch1 = new Branch("b111", "east");
      Branch branch1Copy1 = new Branch("b111", "east");
      Branch branch1Copy2 = new Branch("b111", "east");
      Branch branch1Subtype = new Branch("b111", "east") {
      };
      Branch branch2 = new Branch("b222", "west");

      new EqualityTester(branch1, branch1Copy1, branch1Copy2, branch2, branch1Subtype).verify();
   }
}