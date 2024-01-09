package tasks;

public class Task {

  private final String name;
  private boolean done;

  public Task(String name) {
    this.name = name;
    this.done = false;
  }

  public Task(String name, boolean done) {
    this.name = name;
    this.done = done;
  }

  public String getName() {
    return name;
  }

  public boolean isDone() {
    return done;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    if (this.isDone()) {
      string.append("[x]");
    } else {
      string.append("[ ]");
    }
    string.append(" ").append(this.getName());
    return string.toString();
  }

  public void setDone() {
    done = true;
  }
}
