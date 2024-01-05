package exceptions;

public class MissingSecondArgument extends TodoException {

  public MissingSecondArgument() {
    super("Unable to %s: no index provided");
  }
}