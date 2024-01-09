package actors;

import exceptions.MissingSecondArgument;
import exceptions.TodoException;
import tasks.Tasks;

public class RemoveActor extends Actor {

  public RemoveActor() {
    super("remove",
          "r",
          "remove", "Removes a task");
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
    tasks.removeTask(taskId);
  }
}
