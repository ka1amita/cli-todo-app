package exceptions;

public class WrongTodoIndexFormat extends TodoException {

  public WrongTodoIndexFormat() {
    super("Unable to %s: index is not a number");
  }
}