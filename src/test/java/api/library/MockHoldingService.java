package api.library;

import java.util.*;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.MaterialDetails;

import domain.core.ClassificationApiFactory;

public class MockHoldingService extends HoldingService {
   private FakeClassificationApi classificationApi;

   public MockHoldingService() {
      classificationApi = new FakeClassificationApi();
      ClassificationApiFactory.setService(classificationApi);
   }

   public void addTestBookToMaterialService(MaterialDetails material) {
      classificationApi.add(material);
   }

   class FakeClassificationApi implements ClassificationApi {
      private Map<String, MaterialDetails> materials = new HashMap<String, MaterialDetails>();

      public void add(MaterialDetails material) {
         materials.put(material.getSourceId(), material);
      }

      @Override
      public MaterialDetails getMaterialDetails(String sourceId) {
         return materials.get(sourceId);
      }

      @Override
      public Collection<MaterialDetails> allMaterials() {
         return materials.values();
      }
   }
}