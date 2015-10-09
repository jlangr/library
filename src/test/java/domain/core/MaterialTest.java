package domain.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.loc.material.api.*;

public class MaterialTest {
   @Test
   public void typeDefaultsToBook() {
      Material material = new Material("", "", "", "");
      assertEquals(MaterialType.TYPE_BOOK, material.getType());
   }

   public static void assertMaterialsEqual(Material expected, Material actual) {
      assertEquals(expected.getAuthor(), actual.getAuthor());
      assertEquals(expected.getClassification(), actual.getClassification());
      assertEquals(expected.getTitle(), actual.getTitle());
      assertEquals(expected.getYear(), actual.getYear());
   }
}