import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

  private static final String HELP_MESSAGE = "Command Line Todo application\n"
      + "=============================\n"
      + "\n"
      + "Command line arguments:\n"
      + " -l  --list     Lists undone tasks\n"
      + " -la --listall  Lists all tasks\n"
      + " -a  --add      Adds a new task\n"
      + " -r  --remove   Removes a task\n"
      + " -c  --check    Completes a task\n";
  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private File tasksFile = new File("test", "tasks.txt");


  @BeforeEach
  public void setUp() throws IOException {
    System.setOut(new PrintStream(outputStreamCaptor));
    tasksFile.createNewFile();
    Main.FILENAME = new File("test", "tasks.txt").toString();
  }

  @AfterEach
  public void tearDown() {
    System.setOut(standardOut);
    tasksFile.delete();
  }

  @Test
  public void print_help_message_when_no_argument_provided() throws IOException {
    Main.main("");

    assertEquals(HELP_MESSAGE, outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void print_message_followed_by_help_message_when_wrong_argument_provided()
      throws IOException {
    Main.main("wrong");

    assertEquals("Unsupported argument\n\n" + HELP_MESSAGE, outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_listing_not_done_tasks_and_no_task_is_remaining()
      throws IOException {
    Main.main("-l");

    assertEquals("No todos for today! :)\n", outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_listing_all_tasks_and_no_task_is_remaining()
      throws IOException {
    Main.main("-la");

    assertEquals("No todos for today! :)\n", outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_listing_not_done_tasks_and_only_done_tasks_are_remaining()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "done,true\n");

    Main.main("-l");

    assertEquals("No todos for today! :)\n", outputStreamCaptor.toString());
    assertEquals("done,true\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_listing_all_tasks_and_none_is_present()
      throws IOException {
    Main.main("-l");

    assertEquals("No todos for today! :)\n", outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void lists_only_not_done_tasks()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "done,true\nnot done,false\n");

    Main.main("-l");

    assertEquals("2 - [ ] not done\n", outputStreamCaptor.toString());
    assertEquals("done,true\nnot done,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void lists_all_tasks()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "done,true\nnot done,false\n");

    Main.main("-la");

    assertEquals("1 - [x] done\n2 - [ ] not done\n", outputStreamCaptor.toString());
    assertEquals("done,true\nnot done,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void adds_single_task() throws IOException {
    Main.main("-a", "single");
    String expectedMessage = "\"single\" task added\n";

    assertEquals(expectedMessage, outputStreamCaptor.toString());
    assertEquals("single,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void adds_multiple_tasks() throws IOException {
    Main.main("-a", "first", "second");
    String expectedMessage = "\"first\" task added\n"
        + "\"second\" task added\n";

    assertEquals(expectedMessage, outputStreamCaptor.toString());
    assertEquals("first,false\nsecond,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void remove_single_tasks() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-r", "1");

    assertEquals("\"task\" task removed\n", outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_remove_when_string_instead_of_number_argument()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "original,false\n");
    Main.main("-r", "string");

    assertEquals("Unable to remove: index is not a number\n", outputStreamCaptor.toString());
    assertEquals("original,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_remove_when_argument_out_of_bound()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "original,false\n");
    Main.main("-r", "2");

    assertEquals("Unable to remove: index is out of bound\n", outputStreamCaptor.toString());
    assertEquals("original,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void check_single_tasks() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-c", "1");

    assertEquals("\"task\" task checked\n", outputStreamCaptor.toString());
    assertEquals("task,true\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_check_when_string_instead_of_number_argument()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-c", "string");

    assertEquals("Unable to check: index is not a number\n", outputStreamCaptor.toString());
    assertEquals("task,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void prints_message_when_check_when_argument_out_of_bound() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-c", "2");

    assertEquals("Unable to check: index is out of bound\n", outputStreamCaptor.toString());
    assertEquals("task,false\n", Files.readString(tasksFile.toPath()));
  }
}