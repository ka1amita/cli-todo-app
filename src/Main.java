import exceptions.CantCreateTodoFile;
import exceptions.CantWriteToFile;
import exceptions.MissingSecondArgument;
import exceptions.TodoException;
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

  private static void act(String... args) {
    try {
      tryAct(args);
    } catch (Exception e) {
      System.out.printf((e.getMessage()) + "%n", mode);
    }
  }

  static void tryAct(String... args) throws TodoException {
    setModeByFlag();
    if (mode.equals("list")) {
      tasks.listNotDoneTasks();
    } else if (mode.equals("listall")) {
      tasks.listAllTasks();
    } else if (mode.equals("remove")) {
      int taskId = parseSecondArgumentToTaskId(args);
      tasks.removeTask(taskId);
    } else if (mode.equals("check")) {
      int taskId = parseSecondArgumentToTaskId(args);
      tasks.checkTask(taskId);
    } else if (mode.equals("add")) {
      addTasks(args);
    }
  }

  static void setModeByFlag() {
    mode = MODES.get(flag);
  }

  static void addTasks(String... args) throws TodoException {
    if (args.length == 1) {
      throw new MissingSecondArgument();
    } else {
      for (int i = 1; i < args.length; i++) {
        Task task = new Task(args[i]);
        tasks.addTask(task);
        System.out.println("\"" + task.getName() + "\" task added");
      }
    }
  }

  static int parseSecondArgumentToTaskId(String... args) throws TodoException {
    int taskId;
    try {
      taskId = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      throw new WrongTodoIndexFormat();
    } catch (IndexOutOfBoundsException e) {
      throw new MissingSecondArgument();
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
