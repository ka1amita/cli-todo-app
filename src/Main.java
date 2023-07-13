public class Main {
  public static void main(String[] args) {
    // read tasks from file into Tasks object
    Tasks tasks = new Tasks("test.txt");

    // print usage
    if (args.length == 0) {
      printUsage();
      return;
    }
    // list tasks
    if (args[0].equals("-l")) {
      tasks.printTasks();
      return;
    }

    // add new task(s)
    if (args[0].equals("-a")) {
      for (int i = 1; i < args.length; i++) {
        new Task(args[i]);
      }
      return;
    }


  }

  private static void printUsage() {
    System.out.println(
        "Command Line Todo application\n" +
            "=============================\n" +
            "\n" +
            "Command line arguments:\n" +
            "    -l   Lists all the tasks\n" +
            "    -a   Adds a new task\n" +
            "    -r   Removes an task\n" +
            "    -c   Completes an task"
    );
  }
}
