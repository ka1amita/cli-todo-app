import actors.CheckActor;
import actors.ListActor;
import actors.ListAllActor;
import actors.RemoveActor;
import tasks.Tasks;
import actors.Actor;
import actors.AddActor;
import exceptions.CantWriteToFile;
import exceptions.MissingTodoFlag;
import exceptions.TodoException;
import exceptions.UnsupportedTodoOperation;
import exceptions.UnsupportedTodoAgument;
import java.util.Set;

public class Main {

  static String FILENAME = "tasks.txt";
  private static String flag = "";
  private static String mode = "";
  private static Tasks tasks;
  private static Set<Actor> actors = new HashSet<>();
  private static Actor actor;
  private static String[] args;

  public static void main(String... input) {
    args = input;
    populateActors();
    if (!parseFlag()) {
      return;
    }
    if (!setActorFromFlag()) {
      return;
    }
    readTasksFromFile();
    act();
    writeToFile();
  }

  private static boolean setActorFromFlag() {
    try {
      trySetActorFromFlag();
      return true;
    } catch (UnsupportedTodoOperation e) {
      System.out.format(e.getMessage(), args[0]);
      printUsage();
    } catch (TodoException e) {
      System.out.printf(e.getMessage(), mode);
    }
    return false;
  }

  private static boolean parseFlag() {
    try {
      tryParseFlag();
      return true;
    } catch (UnsupportedTodoAgument e) {
      System.out.format(e.getMessage());
      System.out.println();
      System.out.println();
      printUsage();
      return false;
    } catch (TodoException e) {
      printUsage();
      return false;
    }
  }

  private static void act() {
    try {
      actor.act(tasks);
    } catch (TodoException e) {
      System.out.format(e.getMessage(), actor.getType());
      System.out.println();
    }
  }

  private static void populateActors() {
    actors.add(new AddActor());
    actors.add(new RemoveActor());
    actors.add(new CheckActor());
    actors.add(new ListAllActor());
    actors.add(new ListActor());
  }

  static void tryParseFlag() throws TodoException {
    String flagArgument;
    flagArgument = readFirstArgument();
    parseFlagArgument(flagArgument);
  }

  private static String readFirstArgument() throws MissingTodoFlag {
    if (args.length == 0 || args[0].isEmpty()) {
      throw new MissingTodoFlag();
    }
    return args[0];
  }

  private static void parseFlagArgument(String flagArgument) throws UnsupportedTodoAgument {
    if (flagArgument.matches("--\\w+")) {
      flag = flagArgument.substring(2);
    } else if (flagArgument.matches("-\\w+")) {
      flag = flagArgument.substring(1, 2);
    } else {
      throw new UnsupportedTodoAgument();
    }
  }

  static void trySetActorFromFlag() throws TodoException {
    actor = ACTORS.stream()
        .filter(a -> a.getShortFlag().equals(flag) || a.getLongFlag().contentEquals(flag))
        .findFirst()
        .orElseThrow(UnsupportedTodoOperation::new);
    actor.setArgument(args);
  }

  static void readTasksFromFile() {
    try {
      tasks = new Tasks(FILENAME);
    } catch (TodoException e) {
      System.out.println(e.getMessage());
    }
  }

  static void writeToFile() {
    try {
      tasks.writeToFile();
    } catch (CantWriteToFile e) {
      System.out.println(e.getMessage());
    }
  }

  static void printUsage() {
    String prefix = "Command Line Todo application\n"
        + "=============================\n"
        + "\n"
        + "Command line arguments:\n";

    StringBuilder usage = new StringBuilder(prefix);

    for (Actor actor : ACTORS) {
      usage.append(actor.getUsage()).append("\n");
    }

    System.out.print(usage);
  }
}
