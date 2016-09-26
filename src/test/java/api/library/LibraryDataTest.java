package api.library;

import static org.junit.Assert.assertTrue;
import com.loc.material.api.*;
import domain.core.*;
import static org.mockito.Mockito.*;
import org.junit.*;

public class LibraryDataTest {
   private PatronService patronService = new PatronService();
   private HoldingService holdingService = new HoldingService();
   private BranchService branchService = new BranchService();
   private ClassificationApi classificationApi;

   @Before
   public void setUpClassificationService() {
      classificationApi = mock(ClassificationApi.class);
      ClassificationApiFactory.setService(classificationApi);
   }

   @Test
   public void deleteAllRemovesAllPatrons() {
      patronService.patronAccess.add(new Patron("", "1"));
      branchService.add("2");
      MaterialDetails material = new MaterialDetails("3", "", "", "", "");
      when(classificationApi.getMaterialDetails("3")).thenReturn(material);
      holdingService.add(material.getSourceId(), Branch.CHECKED_OUT.getScanCode());

      LibraryData.deleteAll();

      assertTrue(patronService.allPatrons().isEmpty());
      assertTrue(holdingService.allHoldings().isEmpty());
      assertTrue(branchService.allBranches().isEmpty());
   }
}
