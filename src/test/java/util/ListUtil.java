package util;

import java.lang.reflect.Method;
import java.util.*;

public class ListUtil {
   public <ListType,ToType> List<ToType> map(
         List<ListType> c,
         String methodName,
         Class<ListType> listClass,
         Class<ToType> toClass) {
      Method method = getMethod(methodName, listClass);
      List<ToType> results = new ArrayList<>();
      for (ListType each: c)
         results.add(invokeMethod(method, each, toClass));
      return results;
   }

   private <ListType,ToType> ToType invokeMethod(Method method, ListType receiver, Class<ToType> toTypeClass) {
      try {
         return toTypeClass.cast(method.invoke(receiver));
      } catch (Exception e) {
         throw new RuntimeException("unable to invoked " + method);
      }
   }

   private <T> Method getMethod(String method, Class<T> klass) {
      try {
         return klass.getMethod(method);
      } catch (Exception e) {
         throw new RuntimeException("invalid method");
      }
   }
}
