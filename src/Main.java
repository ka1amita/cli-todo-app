import java.util.HashMap;

public class Main {

  public static final HashMap<String, String> COMMANDS = new HashMap<>() {{
    put("-l", "-l");
    put("--list", "-l");
    put("-la", "-l");
    put("--listall", "-la");
    put("-r", "-r");
    put("--remove", "-r");
    put("-c", "-c");
    put("--check", "-c");
    put("-a", "-a");
    put("--add", "-a");
  }};

  // TODO https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
  //  class Test {
  //     public static void main(String[] args) throws Exception {
  //         Map<Character, Runnable> commands = new HashMap<>();
  //         // Populate commands map
  //         commands.put('h', () -> System.out.println("Help"));
  //         commands.put('t', () -> System.out.println("Teleport"));
  //         // Invoke some command
  //         char cmd = 't';
  //         commands.get(cmd).run();   // Prints "Teleport"
  //     }
  // }

  public static void main(String[] args) {
    // read tasks from file into Tasks object
    Tasks tasks = new Tasks("test.txt"); // TODO read from config

    // print usage
    if (args.length == 0) {
      printUsage();
      return;
    }
    // arg validation
    if (!COMMANDS.containsKey(args[0])) {
      System.out.println("Unsupported argument");
      System.out.println();
      printUsage();
      return;
    }
    // parse long arg to short arg
    args[0] = COMMANDS.get(args[0]);

    // list undone tasks
    if (args[0].equals("-l")) {
      tasks.listTasks(false);
      return;
    }

    // list all tasks
    if (args[0].equals("-la")) {
      tasks.listTasks();
      return;
    }

    // remove task(s)
    if (args[0].equals("-r") && args.length == 1) {
      System.out.println("Unable to remove: no index provided");
      return;
    }
    if (args[0].equals("-r") && args.length > 1) {
      // TODO handle on the remove() level?
      try {
        for (int i = 1; i < args.length; i++) {
          tasks.remove(Integer.parseInt(args[i]) - 1);
        }
        tasks.writeToFile("output.txt");
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Unable to remove: index is out of bound");
      } catch (NumberFormatException e) {
        System.out.println("Unable to remove: index is not a number");
      }
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
    if (args[0].equals("-a") && args.length == 1) {
      System.out.println("Unable to add: no index provided");
      return;
    }

    if (args[0].equals("-a") && args.length > 1) {
      for (int i = 1; i < args.length; i++) {
        Task task = new Task(args[i]);
        tasks.add(task);
      }
      tasks.writeToFile("output.txt");
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
