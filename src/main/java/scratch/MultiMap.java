package scratch;

import java.util.*;

public class MultiMap {
   private Map<String,List<String>> map = new HashMap<>();

   public boolean isEmpty() {
      return map.isEmpty();
   }

   public void put(String key, String value) {
      if (key == null)
         throw new IllegalArgumentException("key is null");
      List<String> values = get(key);
      if (values.isEmpty())
         map.put(key,  values);
      values.add(value);
   }

   public int size() {
      return map.size();
   }

   public List<String> get(String key) {
      if (!map.containsKey(key))
         return new ArrayList<String>();
      return map.get(key);
   }
}