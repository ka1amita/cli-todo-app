import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Tasks {
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

  public void printTasks() {
    int i = 1;
    for (Task task : tasks) {
      System.out.println(i + " - " + task.toString());
      i++;
    }
  }

  public void readFromFile(String file) {
    String delimiter = ",";

    Path path = Paths.get(file);
    List<String> content = new ArrayList<>();

    try {
      content = Files.readAllLines(path);
    } catch (IOException e) {
      System.out.println("can't read from file");
      System.exit(1);
    }
    for (String line : content) {
      String[] split = line.split(delimiter);
      this.tasks.add(new Task(split[0], Boolean.parseBoolean(split[1])));
    }
  }
}
