package util;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

// Implementation of Soundex (http://en.wikipedia.org/wiki/Soundex)
// The rules, per Wikipedia:
// 1. Retain the first letter of the name and drop all other occurrences of a,e,i,o,u,y,h,w.
// 2. Replace consonants with digits as follows (after the first letter):
//     b, f, p, v => 1
//     c, g, j, k, q, s, x, z => 2
//     d, t => 3
//     l => 4
//     m, n => 5
//     r => 6
// 3. Two adjacent letters (in the original name) with the same number are coded as a single
//    number; also two letters with the same number separated by 'h' or 'w' are coded as a
//    single number, whereas such letters separated by a vowel are coded twice. This rule
//    also applies to the first letter.
// 4. Continue until you have one letter and three numbers. If you run out of letters, fill
//    in 0s until there are three numbers.

// For each test from top to bottom:
// - ensure all tests pass
// - remove the @Ignore annotation
// - run all tests. Ensure the newly-unignored test ("current") fails.
// - get the current test to pass. Build no more implementation than required.
//   Tests will fail if you implement too much.
// Read additional comments in each test and follow any additional rules specified.
// Please do not prematurely un-ignore any tests.

//@RunWith(IncrementalRunner.class)
public class SoundexTest {
   private Soundex soundex;

   @Before
   public void create() {
      soundex = new Soundex();
   }

   @Test
   public void retainsSoleLetterOfOneLetterWord() {
      assertThat(soundex.encode("A"), is(equalTo("A")));
   }

   @Test
   @Ignore
   public void padsWithZerosToEnsureThreeDigits() {
      assertThat(soundex.encode("I"), is(equalTo("I000")));

      // When you get this test to pass, you will need to
      // fix the specification in the prior test.
   }

   @Test
   @Ignore
   public void replacesConsonantsWithAppropriateDigits() {
      assertThat(soundex.encode("Ab"), is(equalTo("A100")));
      // Uncomment each subsequent assertion, and get to pass, one at a time:
      // assertThat(soundex.encode("Bcdl"), is(equalTo("B234")));
      // assertThat(soundex.encode("Ajmr"), is(equalTo("A256")));

      // Prepare to discuss: Should we have multiple assertions in one test?
   }

   @Test
   @Ignore
   public void limitsLengthToFourCharacters() {
      assertThat(soundex.encode("Dbcdlmr"), is(equalTo("D123")));
   }

   @Test
   @Ignore
   public void ignoresVowelLikeLetters() {
      assertThat(soundex.encode("Faeiouhycts"), is(equalTo("F232")));
   }

   @Test
   @Ignore
   public void combinesDuplicateEncodingsAsSingleNumber() {
      // Prepare to discuss:
      // - What is the value of these three preconditions?
      // - What is the cost (tradeoff)? How do you feel about the design choice?
      //assertThat(soundex.toDigit('b'), is(equalTo(soundex.toDigit('f'))));
      //assertThat(soundex.toDigit('c'), is(equalTo(soundex.toDigit('g'))));
      //assertThat(soundex.toDigit('d'), is(equalTo(soundex.toDigit('t'))));

      assertThat(soundex.encode("Gbfcgdt"), is(equalTo("G123")));
   }

   @Test
   @Ignore
   public void uppercasesFirstLetter() {
      assertThat(soundex.encode("abcd"), is(equalTo("A123")));
   }

   @Test
   @Ignore
   public void ignoresVowelLikeLettersRegardlessOfCase() {
      assertThat(soundex.encode("FcAEIOUHYts"), is(equalTo("F232")));
   }

   @Test
   @Ignore
   public void replacesConsonantsWithAppropriateDigitsRegardlessOfCase() {
      assertThat(soundex.encode("BCDL"), is(equalTo("B234")));
   }

   @Test
   @Ignore
   public void combinesDuplicateEncodingsWhenSecondLetterDuplicatesFirst() {
      assertThat(soundex.encode("Bbcd"), is(equalTo("B230")));
   }

   @Test
   @Ignore
   public void doesNotCombineDuplicateEncodingsSeparatedByVowels() {
      assertThat(soundex.encode("Jbobby"), is(equalTo("J110")));
   }
   // Congratulations if you made it this far!
   // Prepare to discuss:
   // - What other tests are missing?
   // - What were the costs and benefits of building Soundex incrementally?
}
