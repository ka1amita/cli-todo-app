package actors;

import exceptions.MissingSecondArgument;
import exceptions.TodoException;
import tasks.Task;
import tasks.Tasks;

public class AddActor extends Actor {

  public AddActor() {
    super("add",
          "a",
          "add",
          "Adds a new task");
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
    Task task = new Task(arg);
    tasks.addTask(task);
  }
}
