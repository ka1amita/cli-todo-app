import java.util.HashMap;

public class Main {

  public static final HashMap<String, String> COMMANDS = new HashMap<>() {{
    put("-l", "list");
    put("--list", "list");
    put("-la", "-listall");
    put("--listall", "-listall");
    put("-r", "remove");
    put("--remove", "remove");
    put("-c", "check");
    put("--check", "check");
    put("-a", "add");
    put("--add", "add");
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
    if (args[0].equals("list")) {
      tasks.listTasks(false);
      return;
    }

    // list all tasks
    if (args[0].equals("listall")) {
      tasks.listTasks();
      return;
    }

    // remove task(s)
    if (args[0].equals("remove")) {
      try {
        callRemove(args, tasks);
      } catch (Exception e) {
        System.out.println(String.format(e.getMessage(),"remove"));
      }
      return;
    }
    // check task(s)
    if (args[0].equals("check")) {
      try {
        callCheck(args, tasks);
      } catch (Exception e) {
        System.out.println(String.format(e.getMessage(),"check"));
      }
      return;
    }
    // add new task(s)
    if (args[0].equals("add")) {
      try {
        callAdd(args, tasks);
      } catch (Exception e) {
        System.out.println(String.format(e.getMessage(),"add"));
      }
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
      throw new TodoException.TodoNullPointerException();
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
      throw new TodoException.TodoNullPointerException();
    } else if (args.length > 1) {
      try {
        for (int i = 1; i < args.length; i++) {
          tasks.remove(Integer.parseInt(args[i]) - 1);
        }
      } catch (IndexOutOfBoundsException e) {
        throw new TodoException.TodoIndexOutOfBoundsException();
      } catch (NumberFormatException e) {
        throw new TodoException.TodoNumberFormatException();
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
