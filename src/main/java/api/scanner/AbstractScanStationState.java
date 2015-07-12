package api.scanner;

abstract public class AbstractScanStationState {
   @Override
   public String toString() {
      return "state: " + getClass().getSimpleName();
   }
}
