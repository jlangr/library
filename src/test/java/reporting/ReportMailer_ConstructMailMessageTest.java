package reporting;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;

import javax.mail.*;

import org.junit.*;

public class ReportMailer_ConstructMailMessageTest {
   private String toAddress;
   private Report report;
   private Session session;
   private Message message;

   @Before
   public void initializeStuff() {
      toAddress = "a@b.com";
      report = mock(Report.class);
      when(report.getText()).thenReturn("text");
      when(report.getName()).thenReturn("subject");
      session = null; // no way to retrieve
   }

   @Test
   public void subjectIsReportName() throws MessagingException {
      message = constructMailMessageTo(toAddress, report, session);

      assertThat(message.getSubject(), is(report.getName()));
   }

   @Test
   public void contentsIsReportText() throws IOException, MessagingException {
      message = constructMailMessageTo(toAddress, report, session);

      assertThat(message.getContent().toString(), is(report.getText()));
   }

   @Test
   public void recipientsContainsToAddress() throws MessagingException {
      message = constructMailMessageTo(toAddress, report, session);

      assertThat(message.getAllRecipients()[0].toString(), is(toAddress));
   }

   @Test
   public void fromIsHardcodedReportMailerValue() throws MessagingException {
      message = constructMailMessageTo(toAddress, report, session);

      assertThat(message.getFrom()[0].toString(), is(ReportMailer.FROM));
   }

   private Message constructMailMessageTo(String toAddress, Report report,
         Session session) {
      try {
         return ReportMailer.constructMailMessageTo(toAddress, report, session);
      } catch (Exception e) {
         throw new AssertionError();
      }
   }
}
