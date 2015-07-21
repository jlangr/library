package portfolio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;

public class PortfolioTest {
   private Portfolio portfolio;

   @Before
   public void create() {
      portfolio = new Portfolio();
   }

   @Test
   public void hasZeroHoldingsWhenCreated() {
      assertThat(new Portfolio().getNumberOfHoldings(), equalTo(0));
   }

   @Test
   public void incrementsHoldingsOnPurchase() {
      portfolio.purchase("CSCO", 10);
      portfolio.purchase("IBM", 20);

      assertThat(portfolio.getNumberOfHoldings(), equalTo(2));
   }

   @Test
   public void returnsNumberOfSharesPurchasedForSymbol() {
      portfolio.purchase("CSCO", 10);

      assertThat(portfolio.sharesOf("CSCO"), equalTo(10));
   }

   @Test
   public void sumsSharesFromMultiplePurchasesForSymbol() {
      portfolio.purchase("CSCO", 10);
      portfolio.purchase("CSCO", 20);

      assertThat(portfolio.sharesOf("CSCO"), equalTo(30));
   }

   @Test
   public void separatesSharesBySymbol() {
      portfolio.purchase("CSCO", 10);
      portfolio.purchase("IBM", 20);

      assertThat(portfolio.sharesOf("CSCO"), equalTo(10));
   }

   @Test
   public void returnsZeroWhenSymbolNotPurchased() {
      portfolio.purchase("CSCO", 20);
      assertThat(portfolio.sharesOf("IBM"), equalTo(0));
   }
}
