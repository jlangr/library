package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import com.loc.material.api.*;
import domain.core.*;

public class HoldingServiceTest {
   @Rule
   public ExpectedException expectedEx = ExpectedException.none();

   private HoldingService service = new HoldingService();
   private ClassificationApi classificationApi = mock(ClassificationApi.class);
   private String branchScanCode;

   @Before
   public void initialize() {
      LibraryData.deleteAll();
      ClassificationApiFactory.setService(classificationApi);
      branchScanCode = new BranchService().add("");
   }

   @Test
   public void usesClassificationServiceToRetrieveBookDetails() {
      String isbn = "9780141439594";
      MaterialDetails material = new MaterialDetails(isbn, "", "", "", "");
      when(classificationApi.getMaterialDetails(isbn)).thenReturn(material);
      String barcode = service.add(isbn, branchScanCode);

      Holding holding = service.find(barcode);

      assertThat(holding.getMaterial().getSourceId(), equalTo(isbn));
   }

   @Test
   public void throwsExceptionWhenBranchNotFound() {
      expectedEx.expect(BranchNotFoundException.class);
      expectedEx.expectMessage("Branch not found: badBranchId");

      service.add("", "badBranchId");
   }
}