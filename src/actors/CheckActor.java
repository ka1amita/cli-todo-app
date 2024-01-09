package actors;

import exceptions.MissingSecondArgument;
import exceptions.TodoException;
import tasks.Tasks;

public class CheckActor extends Actor {

  public CheckActor() {
    super("check",
          "c",
          "check",
          "Check a task");
  }

  @Override
  public void act(Tasks tasks) throws TodoException {
    String arg;
    try {
      arg = args[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new MissingSecondArgument();
    }
    // TODO support multiple operations at once
    int taskId = parseSecondArgumentToTaskId(arg);
    tasks.checkTask(taskId);
  }
}
