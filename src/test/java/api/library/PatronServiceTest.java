package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;
import persistence.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import org.mockito.Mock;
import domain.core.*;

public class PatronServiceTest {
   static String RAVI = "Ravi Sankaran";

   PatronService service;
   @Mock private CreditVerifier verifier;
   public static final String VALID_CARD = "9999";
   public static final String INVALID_CARD = VALID_CARD + "x";

   @Before
   public void initialize() {
      initMocks(this);
      PatronStore.deleteAll();
      service = new PatronService();
      service.setVerifier(verifier);
   }

   @Test
   public void answersGeneratedId() {
      when(verifier.isValid(VALID_CARD)).thenReturn(true);
      String scanCode = service.add("name", VALID_CARD);
      assertTrue(scanCode.startsWith("p"));
   }

   @Test
   public void allowsAddingPatronWithId() {
      service.addWithValidCredit("xyz", "p123");

      Patron patron = service.find("p123");

      assertThat(patron.getName(), is("xyz"));
   }

   @Test(expected=InvalidPatronIdException.class)
   public void rejectsPatronIdNotStartingWithP() {
      service.addWithValidCredit("", "234");
   }

   @Test(expected=DuplicatePatronException.class)
   public void rejectsAddOfDuplicatePatron() {
      service.addWithValidCredit("", "p556");
      service.addWithValidCredit("", "p556");
   }

   @Test
   public void addPatronStoresPatronWithValidCard() {
      when(verifier.isValid(VALID_CARD)).thenReturn(true);

      String id = service.add(RAVI, VALID_CARD);

      assertThat(service.find(id).getName(), is(RAVI));
   }

   @Test
   public void addPatronStoresPatronWhenVerifierThrows() {
      when(verifier.isValid(anyString())).thenThrow(new RuntimeException());

      String id = service.add(RAVI, "adsfdj");

      assertThat(service.find(id).getName(), is(RAVI));

   }

   @Test
   public void addPatronDoesNotStorePatronWithInvalidCard() {
      when(verifier.isValid(INVALID_CARD)).thenReturn(false);
      String id = service.add(RAVI, INVALID_CARD);

      assertThat(service.find(id), is(nullValue()));
   }

   @Test
   public void answersNullWhenPatronNotFound() {
      assertNull(service.find("nonexistent id"));
   }
}
