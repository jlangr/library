package api.scanner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class BarcodeInterpreterTest {
   @Test
   public void returnsHoldingTypeWhenBarcodeContainsColon() {
      assertThat(BarcodeInterpreter.typeOf("123:1"), equalTo(BarcodeType.Holding));
   }

   @Test
   public void returnsBranchTypeWhenStartsWithB() {
      assertThat(BarcodeInterpreter.typeOf("b123"), equalTo(BarcodeType.Branch));
      assertThat(BarcodeInterpreter.typeOf("B123"), equalTo(BarcodeType.Branch));
   }

   @Test
   public void returnsInventoryTypeWhenStartsWithI() {
      assertThat(BarcodeInterpreter.typeOf("i111"), equalTo(BarcodeType.Inventory));
      assertThat(BarcodeInterpreter.typeOf("I111"), equalTo(BarcodeType.Inventory));
   }

   @Test
   public void returnsPatronTypeWhenStartsWithP() {
      assertThat(BarcodeInterpreter.typeOf("p123"), equalTo(BarcodeType.Patron));
   }

   @Test
   public void returnsUnrecognizedTypeWhenOther() {
      assertThat(BarcodeInterpreter.typeOf("123"), equalTo(BarcodeType.Unrecognized));
   }
}
