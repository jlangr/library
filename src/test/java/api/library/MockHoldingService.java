package api.library;

import java.util.*;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.MaterialDetails;
import com.loc.material.api.MaterialType;

import domain.core.Book;
import domain.core.ClassificationApiFactory;

public class MockHoldingService extends HoldingService {
   private FakeClassificationApi classificationApi;

   public MockHoldingService() {
      classificationApi = new FakeClassificationApi();
      ClassificationApiFactory.setService(classificationApi);
   }

   public void addTestBookToMaterialService(Book book) {
      addTestBookToMaterialService(book.getClassification(), book.getAuthor(),
            book.getTitle(), MaterialType.Book, book.getYear());
   }

   public void addTestBookToMaterialService(String classification,
         String author, String title, MaterialType type, String year) {
      MaterialDetails material = new MaterialDetails(author, title,
            classification, type, year);
      classificationApi.add(material);
   }

   class FakeClassificationApi implements ClassificationApi {
      private Map<String, MaterialDetails> materials = new HashMap<String, MaterialDetails>();

      @Override
      public boolean isValid(String classification) {
         return materials.containsKey(classification);
      }

      public void add(MaterialDetails material) {
         materials.put(material.getClassification(), material);
      }

      @Override
      public MaterialDetails getMaterialDetails(String classification) {
         return materials.get(classification);
      }

      @Override
      public Collection<MaterialDetails> allMaterials() {
         return materials.values();
      }
   }
}