package exceptions;

public class MissingArgument extends TodoException {

  public MissingArgument() {
    super("Unable to %s: no index provided");
  }
}