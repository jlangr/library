package api.library;

import java.util.Collection;

import persistence.PatronStore;
import domain.core.Patron;

public class PatronService {
   public PatronStore patronAccess = new PatronStore();
   private CreditVerifier creditVerifier;

   public String addWithValidCredit(String name) {
      return save(new Patron(name));
   }

   public String addWithValidCredit(String name, String id) {
      if (!id.startsWith("p")) throw new InvalidPatronIdException();
      return save(new Patron(name, id));
   }

   private String save(Patron newPatron) {
      if (patronAccess.find(newPatron.getId()) != null)
         throw new DuplicatePatronException();
      patronAccess.add(newPatron);
      return newPatron.getId();
   }

   public String add(String name, String cardNumber) {
      if (isValid(cardNumber))
         return addWithValidCredit(name);
      return "";
   }
  

   private boolean isValid(String cardNumber) {
      if (creditVerifier == null)
         return true;

      try {
         return creditVerifier.isValid(cardNumber);
      } catch (Exception e) {
         return true;
      }
   }

   public Patron find(String id) {
      return patronAccess.find(id);
   }

   public Collection<Patron> allPatrons() {
      return patronAccess.getAll();
   }

   public void setVerifier(CreditVerifier creditVerifier) {
      this.creditVerifier = creditVerifier;
   }
}
