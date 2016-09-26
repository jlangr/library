package testutil;

import java.util.Collection;
import java.util.Iterator;
import org.junit.*;

public class CollectionsUtil {
   static final String NO_ELEMENTS = "no elements";
   static final String MORE_THAN_ONE_ELEMENT = "more than one element";

   public static <T> T soleElement(Collection<T> collection) {
      Iterator<T> it = collection.iterator();
      Assert.assertTrue(NO_ELEMENTS, it.hasNext());
      T sole = it.next();
      Assert.assertFalse(MORE_THAN_ONE_ELEMENT, it.hasNext());
      return sole;
   }
}