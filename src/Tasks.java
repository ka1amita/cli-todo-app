import exceptions.CantCreateTodoFile;
import exceptions.CantWriteToFile;
import exceptions.TodoIndexOutOfBounds;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Tasks {
  public static final String DELIMITER = ",";
  private static final String NO_TODOS_MESSAGE = "No todos for today! :)";
  private final List<Task> tasks = new ArrayList<>();

  public Tasks(List<Task> tasks) {
    this.tasks.addAll(tasks);
  }

  public Tasks(String file) throws CantCreateTodoFile {
    readFromOrCreateFile(file);
  }

  private static List<String> readTasksFromFile(Path path) {
    List<String> content;
    try {
      content = Files.readAllLines(path);
    } catch (IOException e) {
      System.out.println("can't read from file");
      return null;
      // TODO throw custom exception instead!
    }
    return content;
  }

  private static void printListOrNoTodosMessage(StringBuilder message) {
    if (message.length() == 0) {
      System.out.println(NO_TODOS_MESSAGE);
    } else {
      System.out.print(message);
    }
  }

  private static void createFile(Path path) throws CantCreateTodoFile {
    try {
      Files.createFile(path);
    } catch (IOException e) {
      throw new CantCreateTodoFile();
    }
    System.out.println("new file created");
  }

  public void readFromOrCreateFile(String filename) throws CantCreateTodoFile {
    Path path = Paths.get(filename);

    if (!Files.exists(path)) {
      System.out.println("Todo file not found, creating new file");
      createFile(path);
    } else {
      readTasks(path);
    }
  }

  private void readTasks(Path path) {
    List<String> content;
    content = readTasksFromFile(path);
    if (content == null) {
      return;
    }
    for (String line : content) {
      String[] split = line.split(DELIMITER);
      this.tasks.add(new Task(split[0], Boolean.parseBoolean(split[1])));
    }
  }

  public void writeToFile(String file) throws CantWriteToFile {
    Path path = Paths.get(file);
    List<String> content = parseToContent();

    try {
      Files.write(path, content);
    } catch (IOException e) {
      throw new CantWriteToFile();
    }
  }

  public Task getAndRemoveTask(int i) throws TodoIndexOutOfBounds {
    Task task = getTask(i);
    removeTask(i);
    return task;
  }

  private List<String> parseToContent() {
    List<String> content = new ArrayList<>();
    for (Task task : tasks) {
      content.add(String.join(DELIMITER, task.getName(), String.valueOf(task.isDone())));
    }
    return content;
  }

  public void add(Task task) {
    tasks.add(task);
  }

  public void removeTask(int taskId) throws TodoIndexOutOfBounds {
    try {
      tasks.remove(taskId - 1);
    } catch (Exception IndexOutOfBoundsException) {
      throw new TodoIndexOutOfBounds();
    }
  }

  public Task getTask(int i) throws TodoIndexOutOfBounds {
    try {
      return tasks.get(i - 1);
    } catch (Exception IndexOutOfBoundsException) {
      throw new TodoIndexOutOfBounds();
    }
  }

  public void listAllTasks() {
    StringBuilder message = new StringBuilder();
    for (int i = 0; i < tasks.size(); i++) {
      message.append(String.format("%d - %s\n", i + 1, tasks.get(i).toString()));
    }

    printListOrNoTodosMessage(message);
  }

  public void listNotDoneTasks() {
    StringBuilder message = new StringBuilder();
    for (int i = 0; i < tasks.size(); i++) {
      if (!tasks.get(i).isDone()) {
        message.append(String.format("%d - %s\n", i + 1, tasks.get(i).toString()));
      }
    }

    printListOrNoTodosMessage(message);
  }

  public Task getAndCheckTask(int taskId) throws TodoIndexOutOfBounds {
    Task task = getTask(taskId);
    task.setDone();
    return task;
  }
}
