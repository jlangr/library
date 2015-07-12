package domain.core.calculators;

import java.util.Date;

import util.DateUtil;
import domain.core.Book;

public class DueDateCalculator {

   public Date due(int type, Date checkoutDate) {
      int period = 0;
      switch (type) {
         case Book.TYPE_BOOK:
            period = Book.BOOK_CHECKOUT_PERIOD;
            break;
         case Book.TYPE_MOVIE:
            period = Book.MOVIE_CHECKOUT_PERIOD;
            break;
         case Book.TYPE_NEW_RELEASE:
            period = Book.NEW_RELEASE_CHECKOUT_PERIOD;
            break;
         default:
            period = Book.BOOK_CHECKOUT_PERIOD;
            break;
      }
      return DateUtil.addDays(checkoutDate, period);
   }
}
