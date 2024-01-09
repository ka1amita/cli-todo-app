package exceptions;

public class MissingTodoFlag extends TodoException {

  public MissingTodoFlag() {
    super("Unable to %s: no index provided");
  }
}
