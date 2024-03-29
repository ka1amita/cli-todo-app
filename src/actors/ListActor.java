package actors;

import tasks.Tasks;

public class ListActor extends Actor {

  public ListActor() {
    super("list",
          "l",
          "list",
          "Lists undone tasks");
  }

  @Override
  public void act(Tasks tasks) {
    tasks.listNotDoneTasks();
  }
}
