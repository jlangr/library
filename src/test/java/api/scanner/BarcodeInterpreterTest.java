package api.scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BarcodeInterpreterTest {
   @Test
   public void returnsHoldingTypeWhenBarcodeContainsColon() {
      assertThat(BarcodeInterpreter.typeOf("123:1"), is(BarcodeType.Holding));
   }

   @Test
   public void returnsBranchTypeWhenStartsWithB() {
      assertThat(BarcodeInterpreter.typeOf("b123"), is(BarcodeType.Branch));
      assertThat(BarcodeInterpreter.typeOf("B123"), is(BarcodeType.Branch));
   }

   @Test
   public void returnsInventoryTypeWhenStartsWithI() {
      assertThat(BarcodeInterpreter.typeOf("i111"), is(BarcodeType.Inventory));
      assertThat(BarcodeInterpreter.typeOf("I111"), is(BarcodeType.Inventory));
   }

   @Test
   public void returnsPatronTypeWhenStartsWithP() {
      assertThat(BarcodeInterpreter.typeOf("p123"), is(BarcodeType.Patron));
   }
   
   @Test
   public void returnsUnrecognizedTypeWhenOther() {
      assertThat(BarcodeInterpreter.typeOf("123"), is(BarcodeType.Unrecognized));
   }
}
