package exceptions;

public class TodoIndexOutOfBounds extends TodoException{

  public TodoIndexOutOfBounds() {
    super("Unable to %s: index is out of bound");
  }

}
