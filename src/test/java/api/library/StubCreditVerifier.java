package api.library;

public class StubCreditVerifier implements CreditVerifier {

   @Override
   public boolean isValid(String cardNumber) {
      return cardNumber.equals(PatronServiceTest.VALID_CARD);
   }

}
