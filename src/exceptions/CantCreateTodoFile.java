package exceptions;

public class CantCreateTodoFile extends TodoException {

  public CantCreateTodoFile() {
    super("Can't create a file");
  }
}