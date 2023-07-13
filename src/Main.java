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
      tasks.listTasks();
      return;
    }
    // remove task(s)
    if (args[0].equals("-r")) {
      for (int i = 1; i < args.length; i++) {
        tasks.remove(Integer.parseInt(args[i]) - 1);
      }
      tasks.writeToFile("output.txt");
      return;
    }
    // check task(s)
    if (args[0].equals("-c")) {
      for (int i = 1; i < args.length; i++) {
        tasks.getTask(Integer.parseInt(args[i]) - 1).setDone();
      }
      tasks.writeToFile("output.txt");
      return;
    }
    // add new task(s)
    if (args[0].equals("-a")) {
      for (int i = 1; i < args.length; i++) {
        Task task = new Task(args[i]);
        tasks.add(task);
      }
      tasks.writeToFile("output.txt");
      return;
    }
    if (args.length != 0) {
      // TODO error.log
      // try {
      //   throw new IllegalArgumentException("Unsupported argument");
      // } catch (IllegalArgumentException e) {
      //   System.out.println(e.getMessage());
      // }
      System.out.println("Unsupported argument");
      System.out.println();
      printUsage();
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
