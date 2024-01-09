import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class MainTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private final File tasksFile = new File("test", "tasks.txt");


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
  public void creates_file_when_tasked_with_access_task_and_file_is_missing() throws IOException {
    tasksFile.delete();
    Main.main("-l");
    String expected = "Source file not found, creating new file\n"
        + "new file created\n"
        + "No todos for today! :)\n";
    assertEquals(expected, outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }
  @Test
  public void print_usage_message_when_no_argument_provided() throws IOException {
    Main.main("");
    assertTrue(outputStreamCaptor.toString().contains(" -L  --  listall     Lists all tasks\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -l  --     list     Lists undone tasks\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -r  --   remove     Removes a task\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -c  --    check     Check a task\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -a  --      add     Adds a new task\n"));
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void print_message_followed_by_usage_message_when_wrong_argument_provided()
      throws IOException {
    Main.main("wrong");

    assertTrue(outputStreamCaptor.toString().startsWith("Unsupported argument\n\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -L  --  listall     Lists all tasks\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -l  --     list     Lists undone tasks\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -r  --   remove     Removes a task\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -c  --    check     Check a task\n"));
    assertTrue(outputStreamCaptor.toString().contains(" -a  --      add     Adds a new task\n"));
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
    Main.main("-L");

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
  public void lists_only_not_done_tasks_with_long_flag()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "done,true\nnot done,false\n");

    Main.main("--list");

    assertEquals("2 - [ ] not done\n", outputStreamCaptor.toString());
    assertEquals("done,true\nnot done,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void lists_all_tasks()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "done,true\nnot done,false\n");

    Main.main("-L");

    assertEquals("1 - [x] done\n2 - [ ] not done\n", outputStreamCaptor.toString());
    assertEquals("done,true\nnot done,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void lists_all_tasks_with_long_flag()
      throws IOException {
    Files.writeString(tasksFile.toPath(), "done,true\nnot done,false\n");

    Main.main("--listall");

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
  public void adds_single_task_with_long_flag() throws IOException {
    Main.main("--add", "single");
    String expectedMessage = "\"single\" task added\n";

    assertEquals(expectedMessage, outputStreamCaptor.toString());
    assertEquals("single,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void adds_multiple_tasks() throws Exception {
    Main.main("-a", "first", "second");
    String expectedMessage = "\"first\" task added\n"
        + "\"second\" task added\n";
    // TODO the test doesn't fails when the assertion passes!
    try {
      assertEquals(expectedMessage, outputStreamCaptor.toString());
      assertEquals("first,false\nsecond,false\n", Files.readString(tasksFile.toPath()));
      fail("Should not add multiple tasks");
    } catch (AssertionFailedError expected) {
    }
  }

  @Test
  public void adds_multiple_tasks_with_long_flag() throws Exception {
    Main.main("--add", "first", "second");
    String expectedMessage = "\"first\" task added\n"
        + "\"second\" task added\n";
    // TODO the test doesn't fails when the assertion passes!
    try {
      assertEquals(expectedMessage, outputStreamCaptor.toString());
      assertEquals("first,false\nsecond,false\n", Files.readString(tasksFile.toPath()));
      fail("Should not add multiple tasks");
    } catch (AssertionFailedError expected) {
    }
  }

  @Test
  public void prints_message_when_add_and_argument_is_missing() throws IOException {
    Main.main("-a");

    assertEquals("Unable to add: no index provided\n", outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void remove_single_tasks() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-r", "1");

    assertEquals("\"task\" task removed\n", outputStreamCaptor.toString());
    assertEquals("", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void remove_single_tasks_with_long_flag() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("--remove", "1");

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
  public void prints_message_when_remove_and_argument_is_missing() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-r");

    assertEquals("Unable to remove: no index provided\n", outputStreamCaptor.toString());
    assertEquals("task,false\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void check_single_tasks() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-c", "1");

    assertEquals("\"task\" task checked\n", outputStreamCaptor.toString());
    assertEquals("task,true\n", Files.readString(tasksFile.toPath()));
  }

  @Test
  public void check_single_tasks_with_long_flag() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("--check", "1");

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

  @Test
  public void prints_message_when_check_and_argument_is_missing() throws IOException {
    Files.writeString(tasksFile.toPath(), "task,false\n");
    Main.main("-c");

    assertEquals("Unable to check: no index provided\n", outputStreamCaptor.toString());
    assertEquals("task,false\n", Files.readString(tasksFile.toPath()));
  }
}