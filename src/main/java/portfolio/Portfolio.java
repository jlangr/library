package portfolio;

import java.util.*;

public class Portfolio {
   private Map<String,Integer> holdings = new HashMap<>();

   public int getNumberOfHoldings() {
      return holdings.size();
   }

   public void purchase(String symbol, int shares) {
      holdings.put(symbol, sharesOf(symbol) + shares);
   }

   public int sharesOf(String symbol) {
      if (!holdings.containsKey(symbol))
         return 0;
      return holdings.get(symbol);
   }
}
