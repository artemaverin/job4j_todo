package ru.job4j.todo.status;

public enum Status {
    COMPLETED(true),
    NEW(false);

    private boolean status;

    Status(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
