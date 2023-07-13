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

  public void listTasks() {
    int i = 1;
    if (tasks.size() == 0) {
      System.out.println("No todos for today! :)");
      return;
    }
    for (Task task : tasks) {
      System.out.println(i + " - " + task.getName());
      i++;
    }
  }

  public void add(Task task) {
    tasks.add(task);
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
    Path path = Paths.get("output.txt");
    List<String> content = new ArrayList<>();

    for (Task task : tasks) {
      content.add(String.join(DELIMITER, task.getName(), String.valueOf(task.isDone())));
    }

    try {
      Files.write(path, content);
    } catch (IOException e) {
      System.out.println("can't write");
      return;
    }
  }
}
