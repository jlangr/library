package scratch;

import java.util.*;

public class Portfolio {
   private Map<String,Integer> holdings = new HashMap<>();
   private StockLookupService service = new NasdaqLookupService();

   public boolean isEmpty() {
      return holdings.isEmpty();
   }

   public void purchase(String symbol, int shares) {
      holdings.put(symbol, shares + sharesOf(symbol));
   }

   public int size() {
      return holdings.size();
   }

   public int sharesOf(String symbol) {
      if (!holdings.containsKey(symbol)) return 0;
      return holdings.get(symbol);
   }

   public int value() {
      int total = 0;
      for (Map.Entry<String, Integer> holding: holdings.entrySet()) {
         String symbol = holding.getKey();
         int shares = holding.getValue();
         total += service.price(symbol) * shares;
      }
      return total;
   }
}
