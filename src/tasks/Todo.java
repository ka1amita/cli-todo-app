package tasks;

import actors.Actor;
import actors.AddActor;
import actors.CheckActor;
import actors.ListActor;
import actors.ListAllActor;
import actors.RemoveActor;
import exceptions.CantWriteToFile;
import exceptions.MissingTodoFlag;
import exceptions.TodoException;
import exceptions.UnsupportedTodoAgument;
import exceptions.UnsupportedTodoOperation;
import java.util.Set;

public class Todo {

  private static final Set<Actor> ACTORS = Set.of(new AddActor(),
                                                  new RemoveActor(),
                                                  new CheckActor(),
                                                  new ListAllActor(),
                                                  new ListActor());
  private String flag;
  protected final String filename;
  private Tasks tasks;
  private Actor actor;
  private String[] args;

  public Todo(String filename) {
    this.filename = filename;
  }

  public void run(String... input) {
    args = input;
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

  private boolean setActorFromFlag() {
    try {
      trySetActorFromFlag();
      return true;
    } catch (UnsupportedTodoOperation e) {
      System.out.format(e.getMessage(), args[0]);
      printUsage();
    } catch (TodoException e) {
      System.out.format(e.getMessage());
    }
    return false;
  }

  private boolean parseFlag() {
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

  private void act() {
    try {
      actor.act(tasks);
    } catch (TodoException e) {
      System.out.format(e.getMessage(), actor.getType());
      System.out.println();
    }
  }

  void tryParseFlag() throws TodoException {
    String flagArgument = readFirsArgument();
    parseFlagArgument(flagArgument);
  }

  private void parseFlagArgument(String flagArgument) throws UnsupportedTodoAgument {
    if (flagArgument.matches("--\\w+")) {
      flag = flagArgument.substring(2);
    } else if (flagArgument.matches("-\\w+")) {
      flag = flagArgument.substring(1, 2);
    } else {
      throw new UnsupportedTodoAgument();
    }
  }

  private String readFirsArgument() throws MissingTodoFlag {
    if (args.length == 0 || args[0].isEmpty()) {
      throw new MissingTodoFlag();
    }
    return args[0];
  }

  void trySetActorFromFlag() throws TodoException {
    actor = ACTORS.stream()
        .filter(a -> a.getShortFlag().equals(flag) || a.getLongFlag().contentEquals(flag))
        .findFirst()
        .orElseThrow(UnsupportedTodoOperation::new);
    actor.setArgument(args);
  }

  void readTasksFromFile() {
    try {
      tasks = new Tasks(filename);
    } catch (TodoException e) {
      System.out.println(e.getMessage());
    }
  }

  void writeToFile() {
    try {
      tasks.writeToFile();
    } catch (CantWriteToFile e) {
      System.out.println(e.getMessage());
    }
  }

  void printUsage() {
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
