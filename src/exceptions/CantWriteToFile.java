package exceptions;

public class CantWriteToFile extends TodoException {

  public CantWriteToFile() {
    super("Can't write to a file");
  }
}