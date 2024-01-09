package exceptions;

public class CantReadFromFile extends TodoException {

  public CantReadFromFile() {
    super("Can't read from a file");
  }
}