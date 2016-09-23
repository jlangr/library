package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;

public class HoldingServiceTest {
   static final String TESS_CLASSIFICATION = "123";
   static final String TESS_ISBN = "9780141439594";
   static final String WEST_BRANCH = "West";
   static final String HOLDING_BARCODE = HoldingBarcode.createCode(TESS_CLASSIFICATION, 1);
   private HoldingService service;
   private ClassificationApi classificationApi;
   private MaterialDetails tessMaterial;
   private String westScanCode;

   public MaterialDetails createTessMaterial() {
      return new MaterialDetails(TESS_ISBN,
            "Hardy, Thomas", "Tess of the d'Urbervilles",
            TESS_CLASSIFICATION, MaterialType.Book, "1891");
   }

   @Before
   public void initialize() {
      LibraryData.deleteAll();
      service = new HoldingService();
      classificationApi = mock(ClassificationApi.class);
      ClassificationApiFactory.setService(classificationApi);

      westScanCode = new BranchService().add(WEST_BRANCH);

      tessMaterial = createTessMaterial();
      when(classificationApi.getMaterialDetails(TESS_ISBN)).thenReturn(
            tessMaterial);
   }

   @Test
   public void usesClassificationServiceToRetrieveBookDetails() {
      String barcode = service.add(TESS_ISBN, westScanCode);

      Holding holding = service.find(barcode);

      assertThat(holding.getMaterial().getAuthor(), is(tessMaterial.getAuthor()));
   }

   @Test
   public void throwsExceptionWhenBranchNotFound() {
      try {
         service.add(TESS_ISBN, "b99999");
      } catch (BranchNotFoundException expected) {
         assertThat(expected.getMessage(), is("Branch not found: b99999"));
      }
   }
}
