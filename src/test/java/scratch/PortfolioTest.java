package scratch;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;

public class PortfolioTest {
   @InjectMocks
   private Portfolio portfolio;
   @Mock
   private StockLookupService service;

   private static final int CSCO_SHARE_PRICE = 28;
   private static final int AAPL_SHARE_PRICE = 130;

   @Before
   public void create() {
      portfolio = new Portfolio();
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void numberOfHoldingsIsZeroOnCreation() {
      assertThat(portfolio.size(), equalTo(0));
   }

   @Test
   public void isEmptyOnCreation() {
      assertTrue(portfolio.isEmpty());
   }

   @Test
   public void isNotEmptyAfterPurchase() {
      portfolio.purchase("CSCO", 1);

      assertFalse(portfolio.isEmpty());
   }

   @Test
   public void incrementsSizeOnPurchase() {
      portfolio.purchase("CSCO", 1);

      assertThat(portfolio.size(), equalTo(1));
   }

   @Test
   public void answersSharesForSinglePurchaseOfSymbol() {
      portfolio.purchase("CSCO", 10);

      assertThat(portfolio.sharesOf("CSCO"), equalTo(10));
   }

   @Test
   public void answersTotalSharesForMultiplePurchases() {
      portfolio.purchase("CSCO", 10);
      portfolio.purchase("CSCO", 20);

      assertThat(portfolio.sharesOf("CSCO"), equalTo(30));
   }

   @Test
   public void answersZeroForNonexistentSymbol() {
      assertThat(portfolio.sharesOf("ABC"), equalTo(0));

   }

   @Test
   public void segregatesSharesBySymbol() {
      portfolio.purchase("CSCO", 10);
      portfolio.purchase("AAPL", 40);

      assertThat(portfolio.sharesOf("AAPL"), equalTo(40));
   }

   @Test
   public void worthlessWhenCreated() {
      assertThat(portfolio.value(), equalTo(0));
   }

   @Test
   public void singleSharePurchaseIncreasesWorthByStockPrice() {
      when(service.price("CSCO")).thenReturn(CSCO_SHARE_PRICE);

      portfolio.purchase("CSCO", 1);

      assertThat(portfolio.value(), equalTo(CSCO_SHARE_PRICE));
   }


   @Test
   public void valueIsTotalForAllSymbols() {
      when(service.price("AAPL")).thenReturn(AAPL_SHARE_PRICE);
      when(service.price("CSCO")).thenReturn(CSCO_SHARE_PRICE);

      portfolio.purchase("CSCO", 10);
      portfolio.purchase("AAPL", 20);

      assertThat(portfolio.value(), equalTo(CSCO_SHARE_PRICE * 10 + AAPL_SHARE_PRICE * 20));
   }
}
