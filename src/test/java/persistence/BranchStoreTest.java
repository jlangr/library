package persistence;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
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
      assertTrue(branch.getScanCode().startsWith("b"));
   }

   @Test
   public void assignedIdIsUnique() {
      Branch branchA = new Branch("a");
      store.save(branchA);

      Branch branchB = new Branch("b");
      store.save(branchB);

      assertFalse(branchA.getScanCode().equals(branchB.getScanCode()));
   }

   @Test
   public void doesNotChangeIdIfAlreadyAssigned() {
      Branch branch = new Branch("", "b1964");

      store.save(branch);

      assertThat(branch.getScanCode(), is("b1964"));
   }

   @Test
   public void returnsSavedBranches() {
      store.save(EAST_BRANCH);

      Branch retrieved = store.find(EAST_BRANCH.getName());

      assertEquals(EAST_BRANCH.getName(), retrieved.getName());
      assertEquals(EAST_BRANCH.getScanCode(), retrieved.getScanCode());
   }

   @Test
   public void returnsNewInstanceOfPersistedBranch() {
      store.save(EAST_BRANCH);
      store = new BranchStore();

      Branch retrieved = store.find(EAST_BRANCH.getName());

      assertNotSame(EAST_BRANCH, retrieved);
   }

   @Test
   public void returnsListOfAllBranches() {
      store.save(EAST_BRANCH);

      Collection<Branch> branches = store.getAll();

      assertEquals(EAST_BRANCH, CollectionsUtil.soleElement(branches));
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