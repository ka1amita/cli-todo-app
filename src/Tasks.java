import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Tasks {
  public static final String DELIMITER = ",";
  List<Task> tasks;


  public Tasks(List<Task> tasks) {
    this.tasks = tasks;
  }

  public Tasks(String file) {
    this();
    this.readFromFile(file);
  }

  public Tasks() {
    tasks = new ArrayList<>();
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public Task getTask(int i) {
    return tasks.get(i);
  }

  public void listTasks() {
    if (checkNoTasks()) {
      return;
    }
    int i = 1;
    for (Task task : tasks) {
      System.out.println(i + " - " + task.toString());
      i++;
    }
  }

  public void listTasks(boolean state) {
    if (checkNoTasks()) {
      return;
    }
    int i = 1;
    for (Task task : tasks) {
      if (task.isDone() == state) {
        System.out.println(i + " - " + task.toString());
        i++;
      }
    }
  }

  private boolean checkNoTasks() {
    if (tasks.size() == 0) {
      System.out.println("No todos for today! :)");
      return true;
    }
    return false;
  }


  public void add(Task task) {
    tasks.add(task);
  }

  public void remove(int i) throws TodoException.TodoIndexOutOfBoundsException {
    try {
      tasks.remove(i);
    } catch (Exception IndexOutOfBoundsException) {
      throw new TodoException.TodoIndexOutOfBoundsException();
    }
  }

  public void readFromFile(String file) {

    Path path = Paths.get(file);
    List<String> content;

    try {
      content = Files.readAllLines(path);
    } catch (IOException e) {
      System.out.println("can't read from file");
      return;
    }
    for (String line : content) {
      String[] split = line.split(DELIMITER);
      this.tasks.add(new Task(split[0], Boolean.parseBoolean(split[1])));
    }
  }

  public void writeToFile(String file) {
    Path path = Paths.get(file);
    List<String> content = new ArrayList<>();

    for (Task task : tasks) {
      content.add(String.join(DELIMITER, task.getName(), String.valueOf(task.isDone())));
    }

    try {
      Files.write(path, content);
    } catch (IOException e) {
      System.out.println("can't write to file");
      return;
    }
  }

}
