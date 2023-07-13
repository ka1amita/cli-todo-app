public class Main {
  public static void main(String[] args) {
    // read tasks from file into Tasks object
    Tasks tasks = new Tasks("tasks.txt");
    // lad tasks from file

    // print usage
    if (args.length == 0) {
      printUsage();
    }
    // list tasks
    if (args[0].equals("-l")) {
      tasks.printTasks();
    }

    // add new task(s)
    if (args[0].equals("-a")) {
      for (int i = 1; i < args.length; i++) {
        new Task(args[i]);
      }
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
