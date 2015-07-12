package api.library;

import java.util.Date;

import domain.core.calculators.DueDateCalculator;

public class DueDateCalculatorService {
   public Date dueDate(int typeCode, Date checkoutDate) {
      return new DueDateCalculator().due(typeCode, checkoutDate);
   }
}
