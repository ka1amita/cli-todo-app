package exceptions;

public class UnsupportedTodoOperation extends TodoException {

  public UnsupportedTodoOperation() {
    super("Unsupported argument");
  }
}