package actors;

import exceptions.MissingSecondArgument;
import exceptions.TodoException;
import exceptions.WrongTodoIndexFormat;
import tasks.Tasks;

public abstract class Actor {

  // %[argument_index$][flags][width][.precision]conversion
  private static final String USAGE_FORMAT = " -%1$1s  --%2$9s     %3$s";
  protected final String type;
  protected final String shortFlag;
  protected final String longFlag;
  protected final String usage;
  protected String[] args;

  protected Actor(String type, String shortFlag, String longFlag, String usage) {
    this.type = type;
    this.shortFlag = shortFlag;
    this.longFlag = longFlag;
    this.usage = usage;
  }

  public String[] getArgument() {
    return args;
  }

  public void setArgument(String... args) {
    this.args = args;
  }

  public String getShortFlag() {
    return shortFlag;
  }

  public String getLongFlag() {
    return longFlag;
  }

  public abstract void act(Tasks tasks) throws TodoException;

  protected int parseSecondArgumentToTaskId(String argument) throws TodoException {
    int taskId;
    try {
      taskId = Integer.parseInt(argument);
    } catch (NumberFormatException e) {
      throw new WrongTodoIndexFormat();
    } catch (IndexOutOfBoundsException e) {
      throw new MissingSecondArgument();
    }
    return taskId;
  }

  public String getType() {
    return type;
  }

  public String getUsage() {
    return String.format(USAGE_FORMAT, shortFlag, longFlag, usage);
  }
}

