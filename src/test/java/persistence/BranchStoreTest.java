package persistence;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static util.matchers.HasExactlyItemsInAnyOrder.hasExactlyItemsInAnyOrder;
import java.util.Collection;
import org.junit.*;
import domain.core.Branch;
import testutil.CollectionsUtil;

public class BranchStoreTest {
   private BranchStore store;
   private static final Branch EAST_BRANCH = new Branch("East");

   @Before
   public void initialize() {
      BranchStore.deleteAll();
      store = new BranchStore();
   }

   @Test
   public void assignsIdToBranch() {
      Branch branch = new Branch("name");

      store.save(branch);

      assertThat(branch.getScanCode(), startsWith("b"));
   }

   @Test
   public void assignedIdIsUnique() {
      Branch branchA = new Branch("a");
      store.save(branchA);
      Branch branchB = new Branch("b");

      store.save(branchB);

      assertThat(branchA.getScanCode(), not(equalTo(branchB.getScanCode())));
   }

   @Test
   public void doesNotChangeIdIfAlreadyAssigned() {
      Branch branch = new Branch("b1964", "");

      store.save(branch);

      assertThat(branch.getScanCode(), is("b1964"));
   }

   @Test
   public void returnsSavedBranches() {
      store.save(new Branch("name"));

      Branch retrieved = store.findByName("name");

      assertEquals("name", retrieved.getName());
   }

   @Test
   public void returnsNewInstanceOfPersistedBranch() {
      Branch branch = new Branch("name");
      store.save(branch);
      store = new BranchStore();

      Branch retrieved = store.findByName("name");

      assertThat(branch, not(sameInstance(retrieved)));
   }

   @Test
   public void returnsListOfAllBranches() {
      Branch branch = new Branch("b123", "");
      store.save(branch);

      Collection<Branch> branches = store.getAll();

      assertEquals(branch, CollectionsUtil.soleElement(branches));
      assertThat(branches, hasExactlyItemsInAnyOrder(branch));
   }

   @Test
   public void findsBranchByScanCode() {
      store.save(EAST_BRANCH);

      Branch retrieved = store.findByScanCode(EAST_BRANCH.getScanCode());

      assertThat(retrieved, is(EAST_BRANCH));
   }

   @Test
   public void findsCheckedOutBranch() {
      assertThat(store.findByScanCode(Branch.CHECKED_OUT.getScanCode()),
            is(sameInstance(Branch.CHECKED_OUT)));
   }

   @Test
   public void findsBranchByScanCodeReturnsNullWhenNotFound() {
      assertThat(store.findByScanCode(""), nullValue());
   }
}