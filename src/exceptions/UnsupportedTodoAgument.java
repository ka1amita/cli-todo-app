package exceptions;

public class UnsupportedTodoAgument extends TodoException {

  public UnsupportedTodoAgument() {
    super("Unsupported argument");
  }
}
