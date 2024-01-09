package actors;

import tasks.Tasks;

public class ListAllActor extends Actor {

  public ListAllActor() {
    super("list all",
          "L",
          "listall",
          "Lists all tasks");
  }

  @Override
  public void act(Tasks tasks) {
    tasks.listAllTasks();
  }
}
