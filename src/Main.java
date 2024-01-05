import exceptions.CantCreateTodoFile;
import exceptions.CantWriteToFile;
import exceptions.MissingArgument;
import exceptions.TodoException;
import exceptions.TodoIndexOutOfBounds;
import exceptions.WrongTodoIndexFormat;
import java.util.HashMap;

public class Main {

  private static final HashMap<String, String> MODES = new HashMap<>() {{
    put("-l", "list");
    put("--list", "list");
    put("-la", "listall");
    put("--listall", "listall");
    put("-r", "remove");
    put("--remove", "remove");
    put("-c", "check");
    put("--check", "check");
    put("-a", "add");
    put("--add", "add");
  }};
  private static final String TASK_CHECKED_MESSAGE = "\"%s\" task checked\n";
  private static final String TASK_REMOVED_MESSAGE = "\"%s\" task removed\n";
  static String FILENAME = "tasks.txt";
  private static String flag = "";
  private static String mode = "";
  private static Tasks tasks;

  public static void main(String... args) {
    parseFlag(args);
    if (flag.isEmpty()) {
      printUsage();
      return;
    }
    if (isInvalidFlag()) {
      System.out.println("Unsupported argument\n");
      printUsage();
      return;
    }
    readTasksFromFile();
    act(args);
    writeToFile();
  }

  static void parseFlag(String[] args) {
    try {
      flag = args[0];
    } catch (ArrayIndexOutOfBoundsException ignored) {
    }
  }

  static boolean isInvalidFlag() {
    return !MODES.containsKey(flag);
  }

  static void readTasksFromFile() {
    try {
      tasks = new Tasks(FILENAME);
    } catch (CantCreateTodoFile e) {
      System.out.println(e.getMessage());
    }
  }

  static void act(String[] args) {
    setModeByFlag();
    if (mode.equals("list")) {
      tasks.listNotDoneTasks();
    } else if (mode.equals("listall")) {
      tasks.listAllTasks();
    } else if (mode.equals("remove")) {
      try {
        removeTask(args);
      } catch (Exception e) {
        System.out.printf((e.getMessage()) + "%n", mode);
      }
      // TODO move to tasks.remove()
    } else if (mode.equals("check")) {
      try {
        checkTask(args);
      } catch (Exception e) {
        System.out.printf((e.getMessage()) + "%n", mode);
      }
      // TODO move to tasks.add()
    } else if (mode.equals("add")) {
      try {
        addTask(args);
      } catch (Exception e) {
        System.out.printf((e.getMessage()) + "%n", mode);
      }
    }
  }

  static void setModeByFlag() {
    mode = MODES.get(flag);
  }

  static void addTask(String... args) throws TodoException {
    if (args.length == 1) {
      throw new MissingArgument();
    } else {
      for (int i = 1; i < args.length; i++) {
        Task task = new Task(args[i]);
        tasks.add(task);
        System.out.println("\"" + task.getName() + "\" task added");
      }
    }
  }

  static void checkTask(String... args)
      throws TodoException {
    if (args.length == 1) {
      throw new MissingArgument();
    }

    int taskId = parseSecondArgumentToTaskId(args);
    Task task = getTask(taskId);
    System.out.printf(TASK_CHECKED_MESSAGE, task.getName());
  }

  static Task getTask(int taskId) throws TodoIndexOutOfBounds {
    Task task;
    try {
      task = tasks.getAndCheckTask(taskId);
    } catch (IndexOutOfBoundsException e) {
      throw new TodoIndexOutOfBounds();
    }
    return task;
  }

  static int parseSecondArgumentToTaskId(String... args) throws WrongTodoIndexFormat {
    int taskId;
    try {
      taskId = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      throw new WrongTodoIndexFormat();
    }
    return taskId;
  }

  static void writeToFile() {
    try {
      tasks.writeToFile(FILENAME);
    } catch (CantWriteToFile e) {
      System.out.println(e.getMessage());
    }
  }

  static void removeTask(String... args)
      throws TodoException {
    if (args.length == 1) {
      throw new MissingArgument();
    }

    int taskId = parseSecondArgumentToTaskId(args);
    Task task = getAndRemoveTask(taskId);

    System.out.printf(TASK_REMOVED_MESSAGE, task.getName());
  }

  static Task getAndRemoveTask(int taskId) throws TodoIndexOutOfBounds {
    Task task;
    try {
      task = tasks.getAndRemoveTask(taskId);
      } catch (IndexOutOfBoundsException e) {
      throw new TodoIndexOutOfBounds();
    }
    return task;
  }

  static void printUsage() {
    System.out.println("Command Line Todo application\n"
                           + "=============================\n"
                           + "\n"
                           + "Command line arguments:\n"
                           + " -l  --list     Lists undone tasks\n"
                           + " -la --listall  Lists all tasks\n"
                           + " -a  --add      Adds a new task\n"
                           + " -r  --remove   Removes a task\n"
                           + " -c  --check    Completes a task");
  }
}
