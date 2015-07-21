package portfolio;

public class NasdaqService implements StockLookupService {
   @Override
   public int price(String symbol) {
      throw new RuntimeException("NASDAQ has been hacked. System currently unavailable.");
   }
}
