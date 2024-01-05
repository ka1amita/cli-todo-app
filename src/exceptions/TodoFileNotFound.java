package exceptions;

public class TodoFileNotFound extends TodoException {

  public TodoFileNotFound() {
    super("File with tasks not found");
  }
}
