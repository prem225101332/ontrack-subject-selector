package ontrack;

public class Task {

    public enum Status {
        NOT_STARTED, IN_PROGRESS, COMPLETE, RESUBMIT, FEEDBACK_AVAILABLE
    }

    private final String id;
    private final String title;
    private final Status status;

    public Task(String id, String title, Status status) {
        if (id == null || id.isBlank())    throw new IllegalArgumentException("id required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title required");
        if (status == null)                throw new IllegalArgumentException("status required");
        this.id     = id;
        this.title  = title;
        this.status = status;
    }

    public String getId()    { return id; }
    public String getTitle() { return title; }
    public Status getStatus(){ return status; }
}