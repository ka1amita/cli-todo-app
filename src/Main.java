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
  private static final String outputFile = "output.txt";
  private static final String inputFile = "test.txt";


  public static void main(String[] args) {
    // read tasks from file into Tasks object
    Tasks tasks = new Tasks(inputFile); // TODO read from config

    // print usage
    if (args.length == 0) {
      printUsage();
      return;
    }
    // arg validation
    // TODO https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
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
    if (args[0].equals("-r")) {
      callRemove(args, tasks);
      return;
    }
    // check task(s)
    if (args[0].equals("-c")) {
      try {
        callCheck(args, tasks);
      } catch (Exception e) {
        System.out.println(String.format(e.getMessage(),"check"));
      }
      return;
    }
    // add new task(s)
    if (args[0].equals("-a")) {
      callAdd(args, tasks);
      return;
    }
  }

  private static void callCheck(String[] args, Tasks tasks) throws TodoException.TodoIndexOutOfBoundsException {
    if (args.length == 1) {
      throw new TodoException.TodoNullPointerException();
    } else if (args.length > 1) {
      try {
        for (int i = 1; i < args.length; i++) {
          tasks.getTask(Integer.parseInt(args[i]) - 1).setDone();
        }
      } catch (IndexOutOfBoundsException e) {
        throw new TodoException.TodoIndexOutOfBoundsException();
      } catch (NumberFormatException e) {
        throw new TodoException.TodoNumberFormatException();
      }
      tasks.writeToFile(outputFile);
    }
  }

  private static void callAdd(String[] args, Tasks tasks) {
    if (args.length == 1) {
      System.out.println("Unable to add: no todo provided");
    } else if (args.length > 1) {
      for (int i = 1; i < args.length; i++) {
        Task task = new Task(args[i]);
        tasks.add(task);
      }
      tasks.writeToFile(outputFile);
    }
  }

  private static void callRemove(String[] args, Tasks tasks) {
    if (args.length == 1) {
      System.out.println("Unable to remove: no index provided");
    } else if (args.length > 1) {
      try {
        for (int i = 1; i < args.length; i++) {
          tasks.remove(Integer.parseInt(args[i]) - 1);
        }
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Unable to remove: index is out of bound");
      } catch (NumberFormatException e) {
        System.out.println("Unable to remove: index is not a number");
      }
      tasks.writeToFile(outputFile);
    }
  }

  private static void printUsage() {
    System.out.println(
        "Command Line Todo application\n" + "=============================\n" + "\n" + "Command line arguments:\n" +
            "    -l   Lists all the tasks\n" + "    -a   Adds a new task\n" + "    -r   Removes an task\n" +
            "    -c   Completes an task");
  }
}
