import tasks.Todo;

public class Main {

  static String filename = "tasks.txt";

  public static void main(String... args) {
    Todo todo = new Todo(filename);
    todo.run(args);
  }
}
