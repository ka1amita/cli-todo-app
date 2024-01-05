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

  public String getName() {
    return name;
  }

  public boolean isDone() {
    return done;
  }

  @Override
  public String toString() {
    // use string builder
    String string = "";
    if (this.isDone()) {
      string += "[x]";
    } else {
      string += "[ ]";
    }
    string += " " + this.getName();
    return string;
  }

  public void setDone() {
    done = true;
  }
}
