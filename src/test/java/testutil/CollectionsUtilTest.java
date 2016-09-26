package testutil;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.*;

public class CollectionsUtilTest {
   private Collection<Object> collection;

   @Rule
   public ExpectedException exceptionRule = ExpectedException.none();

   @Before
   public void initialize() {
      collection = new ArrayList<Object>();
   }

   @Test
   public void soleElementRetrievesFirstAndOnlyElement() {
      collection.add("a");

      Object soleElement = CollectionsUtil.soleElement(collection);

      assertThat(soleElement, equalTo("a"));
      CollectionsUtil.assertSoleElement(collection, "a");
   }

   @Test
   public void soleElementThrowsWhenWrongElement() {
      exceptionRule.expect(org.junit.ComparisonFailure.class);
      exceptionRule.expectMessage("expected:<[b]> but was:<[a]>");
      collection.add("a");

      CollectionsUtil.assertSoleElement(collection, "b");
   }

   @Test
   public void soleElementThrowsWhenNoElementsExist() {
      exceptionRule.expect(AssertionError.class);
      exceptionRule.expectMessage(CollectionsUtil.NO_ELEMENTS);

      CollectionsUtil.soleElement(collection);
   }

   @Test
   public void assertSoleElementThrowsTestFailureWhenNoElements() {
      exceptionRule.expect(AssertionError.class);
      exceptionRule.expectMessage(CollectionsUtil.NO_ELEMENTS);

      CollectionsUtil.assertSoleElement(collection, "a");
   }

   @Test
   public void soleElementThrowsWhenMoreThanOneElement() {
      exceptionRule.expect(AssertionError.class);
      exceptionRule.expectMessage(CollectionsUtil.MORE_THAN_ONE_ELEMENT);
      collection.add("a");
      collection.add("b");

      CollectionsUtil.soleElement(collection);
   }

   @Test
   public void assertSoleElementThrowsFailureWhenMoreThanOneElement() {
      exceptionRule.expect(AssertionError.class);
      exceptionRule.expectMessage(CollectionsUtil.MORE_THAN_ONE_ELEMENT);
      collection.add("a");
      collection.add("b");

      CollectionsUtil.assertSoleElement(collection, "a");
   }
}