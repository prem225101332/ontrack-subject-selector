package ontrack;

import java.util.*;
import java.util.stream.Collectors;


public class SubjectSelector {

    private final Map<String, Set<String>> enrollments = new HashMap<>();

    private final Map<String, List<Task>> subjectTasks = new HashMap<>();

    private final Map<String, String> activeSubject = new HashMap<>();

    public void enrollStudent(String studentId, String subjectCode) {
        enrollments.computeIfAbsent(studentId, k -> new LinkedHashSet<>())
                .add(subjectCode);
    }

    public void addTask(String subjectCode, Task task) {
        subjectTasks.computeIfAbsent(subjectCode, k -> new ArrayList<>())
                .add(task);
    }

    public List<String> getSubjectsForStudent(String studentId) {
        if (studentId == null) throw new IllegalArgumentException("studentId must not be null");
        return new ArrayList<>(enrollments.getOrDefault(studentId, Collections.emptySet()));
    }

    public List<Task> selectSubject(String studentId, String subjectCode) {
        if (studentId == null || !enrollments.containsKey(studentId))
            throw new IllegalArgumentException("Unknown student: " + studentId);

        if (!enrollments.get(studentId).contains(subjectCode))
            throw new IllegalStateException(
                    studentId + " is not enrolled in " + subjectCode);

        activeSubject.put(studentId, subjectCode);

        return subjectTasks.getOrDefault(subjectCode, Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
    }

    public String getSelectedSubject(String studentId) {
        return activeSubject.get(studentId);
    }

    public List<Task> getTasksByStatus(String studentId, Task.Status status) {
        String subject = activeSubject.get(studentId);
        if (subject == null)
            throw new IllegalStateException("No subject selected for student: " + studentId);

        return subjectTasks.getOrDefault(subject, Collections.emptyList())
                .stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }
}