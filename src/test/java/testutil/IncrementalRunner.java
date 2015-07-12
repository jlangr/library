package testutil;

import org.junit.*;
import org.junit.AssumptionViolatedException;
import org.junit.internal.runners.model.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import org.junit.runners.model.*;

public class IncrementalRunner extends BlockJUnit4ClassRunner {

   public IncrementalRunner(Class<?> klass) throws InitializationError {
      super(klass);
   }

   @Override
   protected void runChild(FrameworkMethod method, RunNotifier notifier) {
      EachTestNotifier eachNotifier = derivedMakeNotifier(method, notifier);
      if (method.getAnnotation(Ignore.class) != null) {
         runIgnoredTest(method, eachNotifier);
         return;
      }

      eachNotifier.fireTestStarted();
      try {
         methodBlock(method).evaluate();
      } catch (AssumptionViolatedException e) {
         eachNotifier.addFailedAssumption(e);
      } catch (Throwable e) {
         eachNotifier.addFailure(e);
      } finally {
         eachNotifier.fireTestFinished();
      }
   }

   private void runIgnoredTest(FrameworkMethod method, EachTestNotifier eachNotifier) {
      eachNotifier.fireTestStarted();
      runExpectingFailure(method, eachNotifier);
      eachNotifier.fireTestFinished();
   }

   private EachTestNotifier derivedMakeNotifier(FrameworkMethod method, RunNotifier notifier) {
      Description description = describeChild(method);
      return new EachTestNotifier(notifier, description);
   }

   private void runExpectingFailure(
         final FrameworkMethod method, EachTestNotifier notifier) {
      if (runsSuccessfully(method))
         notifier.addFailure(
            new RuntimeException("You've built too much, causing " +
                                 "this ignored test to pass."));
   }

   private boolean runsSuccessfully(final FrameworkMethod method) {
      try {
         methodBlock(method).evaluate();
         return true;
      } catch (Throwable e) {
         return false;
      }
   }
}
