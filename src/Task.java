public class Task {
  private String name;
  private boolean done;

  public Task(String name) {
    this.name = name;
    this.done = false;
  }
  public Task(String name, boolean done) {
    this.name = name;
    this.done = done;
  }

  @Override
  public String toString() {
    return name;
  }
}
