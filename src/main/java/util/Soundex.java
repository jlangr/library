package util;

import java.util.*;

public class Soundex {

   static final int MAX_CODE_LENGTH = 4;

   public String encode(String string) {
      throw new util.NotYetImplementedException();
   }

   Map<Character, Character> digits = new HashMap<Character, Character>();
   {
      putAll("bfpv", '1');
      putAll("cgjkqsxz", '2');
      putAll("dt", '3');
      putAll("l", '4');
      putAll("mn", '5');
      putAll("r", '6');
   }

   private void putAll(String letters, char digit) {
      for (int i = 0; i < letters.length(); i++)
         digits.put(letters.charAt(i), digit);
   }

   boolean isVowel(char letter) {
      return "aeiouy".indexOf(letter) != -1;
   }

   boolean isVowelLike(char letter) {
      return "aeiouywh".indexOf(letter) != -1;
   }
}
