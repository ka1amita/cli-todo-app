public class TodoException extends Exception {

  public static class TodoIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public TodoIndexOutOfBoundsException() {
      super("Unable to %s: index is out of bound");
    }
  }

  public static class TodoNullPointerException extends NullPointerException {
    public TodoNullPointerException() {
      super("Unable to %s: no index provided");
    }
  }

  public static class TodoNumberFormatException extends NumberFormatException {
    public TodoNumberFormatException() {
      super("Unable to %s: index is out of bound");
    }
  }
}