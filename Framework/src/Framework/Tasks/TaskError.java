package Framework.Tasks;

import Framework.Error;

public enum TaskError implements Error {
    DRIVER_ALREADY_BOUND,
    DRIVER_NOT_BOUND_TO_TASK,

    TASK_ALREADY_BOUND,
    TASK_NOT_BOUND,

    TASK_ID_NOT_FOUND,

    NO_ERROR;

    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    @Override
    public String getSource() {
        return source;
    }

    private String source = "FRAMEWORK_TASKS";
}
