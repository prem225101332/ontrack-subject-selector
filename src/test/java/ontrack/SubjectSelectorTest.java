package ontrack;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SubjectSelectorTest {

    private SubjectSelector selector;

    @BeforeEach
    void setUp() {
        selector = new SubjectSelector();
        selector.enrollStudent("s001", "SIT707");
        selector.enrollStudent("s001", "SIT123");
        selector.addTask("SIT707", new Task("1.1P", "Evidence week1 Learning",    Task.Status.NOT_STARTED));
        selector.addTask("SIT707", new Task("1.2P", "Broswer automation",      Task.Status.COMPLETE));
        selector.addTask("SIT707", new Task("2.1C", "Selenium tets case",     Task.Status.IN_PROGRESS));
        selector.addTask("SIT123", new Task("1.1P", "Unit testing using Jnuit", Task.Status.NOT_STARTED));
    }

    @Test
    @DisplayName("Dropdown shows all subjects enrolled by the student")
    void studentSeesTheirEnrolledSubjects() {
        List<String> subjects = selector.getSubjectsForStudent("s001");
        assertEquals(2, subjects.size());
        assertTrue(subjects.contains("SIT707"));
        assertTrue(subjects.contains("SIT123"));
    }

    @Test
    @DisplayName("Unknown student has empty subject list")
    void unknownStudentHasNoSubjects() {
        assertTrue(selector.getSubjectsForStudent("s999").isEmpty());
    }

    @Test
    @DisplayName("Null student ID throws IllegalArgumentException")
    void nullStudentIdThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> selector.getSubjectsForStudent(null));
    }

    @Test
    @DisplayName("Selecting SIT707 loads its 3 tasks")
    void selectSubjectLoadsTasks() {
        assertEquals(3, selector.selectSubject("s001", "SIT707").size());
    }

    @Test
    @DisplayName("Tasks are ordered by task ID alphabetically")
    void tasksAreOrdered() {
        List<Task> tasks = selector.selectSubject("s001", "SIT707");
        assertEquals("1.1P", tasks.get(0).getId());
        assertEquals("1.2P", tasks.get(1).getId());
        assertEquals("2.1C", tasks.get(2).getId());
    }

    @Test
    @DisplayName("Task titles and statuses are correctly loaded")
    void taskDetailsAreCorrect() {
        Task first = selector.selectSubject("s001", "SIT707").get(0);
        assertEquals("Hello World", first.getTitle());
        assertEquals(Task.Status.NOT_STARTED, first.getStatus());
    }

    @Test
    @DisplayName("Student cannot select a subject they are not enrolled in")
    void studentCannotSelectUnenrolledSubject() {
        assertThrows(IllegalStateException.class,
                () -> selector.selectSubject("s001", "SIT999"));
    }

    @Test
    @DisplayName("Unknown student cannot select subject")
    void unknownStudentCannotSelectSubject() {
        assertThrows(IllegalArgumentException.class,
                () -> selector.selectSubject("s999", "SIT707"));
    }

    @Test
    @DisplayName("getSelectedSubject returns null before any selection")
    void noSelectionInitially() {
        assertNull(selector.getSelectedSubject("s001"));
    }

    @Test
    @DisplayName("getSelectedSubject returns last selected subject")
    void selectedSubjectTracked() {
        selector.selectSubject("s001", "SIT707");
        assertEquals("SIT707", selector.getSelectedSubject("s001"));
    }

    @Test
    @DisplayName("Switching subjects updates selected subject")
    void switchingSubjectUpdatesSelection() {
        selector.selectSubject("s001", "SIT707");
        selector.selectSubject("s001", "SIT123");
        assertEquals("SIT123", selector.getSelectedSubject("s001"));
    }

    @Test
    @DisplayName("Can filter tasks by status within selected subject")
    void filterTasksByStatus() {
        selector.selectSubject("s001", "SIT707");
        List<Task> complete = selector.getTasksByStatus("s001", Task.Status.COMPLETE);
        assertEquals(1, complete.size());
        assertEquals("1.2P", complete.get(0).getId());
    }

    @Test
    @DisplayName("Filter returns empty list when no tasks match status")
    void filterReturnsEmptyWhenNoMatch() {
        selector.selectSubject("s001", "SIT707");
        assertTrue(selector.getTasksByStatus("s001", Task.Status.RESUBMIT).isEmpty());
    }

    @Test
    @DisplayName("Filter throws if no subject is currently selected")
    void filterThrowsIfNoSubjectSelected() {
        assertThrows(IllegalStateException.class,
                () -> selector.getTasksByStatus("s001", Task.Status.COMPLETE));
    }
}