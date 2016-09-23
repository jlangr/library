package com.loc.material.api;

import java.util.Collection;

public interface ClassificationApi {
   MaterialDetails getMaterialDetails(String classification);

   Collection<MaterialDetails> allMaterials();
}
