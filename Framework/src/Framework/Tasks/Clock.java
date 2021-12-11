package Framework.Tasks;

import Framework.Error;
import Framework.GeneralError;

import java.util.ArrayList;

/**
 * @author Tyler Wang
 * A implementation of {@link TaskLoop} that synchronizes tasks and regulates how fast tasks run.
 */
public class Clock extends TaskLoop{
    /**
     * A constructor that constructs a {@link Clock}.
     * @param refreshRate The refresh rate that is to be used for the specific {@link Clock}. If refresh rate is -1, then {@link Clock} will move on after all {@link Framework.Opmodes.OpMode Opmodes} are complete.
     */
    public Clock(double refreshRate) {
        this.refreshRate = refreshRate;
        this.taskList = new ArrayList<Task>();
        this.clockCycle = 0;
    }

    /**
     * Finds and returns the specific {@link Task} with a specific id}.
     * @param taskID The id of the {@link Task} that is to be returned.
     * @return The object of the {@link Task} with the id of taskID.
     */
    private int findTask(int taskID) {
        int upperBound = this.taskList.size() - 1;
        int lowerBound = 0;
        int median = (lowerBound + upperBound) / 2;
        int index = -1;

        while(median != lowerBound && index == -1) {
            if(this.taskList.get(median).taskClockID < taskID)
                lowerBound = median;
            else if(this.taskList.get(median).taskClockID > taskID)
                upperBound = median;
            else
                index = median;

            median = (lowerBound + upperBound) / 2;
        }

        if(index == -1 && this.taskList.get(median).taskClockID == taskID)
            index = median;
        else if (index == -1 && this.taskList.get(upperBound).taskClockID == taskID)
            index = upperBound;

        return index;
    }

    /**
     * An implementation of the {@link TaskLoop#loop()} function that will be run to check whether to move on to the next cycle.
     * @return {@link GeneralError#NO_ERROR}: logic ran without encountering any errors.
     */
    @Override
    public final Error loop() {
        if(this.refreshRate == -1 || this.refreshRate >= 1000) {
            if(!this.isClockReady())
                return GeneralError.NO_ERROR;
        }
        else {
            while(System.currentTimeMillis() - this.lastRefresh < 1000 / this.refreshRate && !super.isInterrupted());

            if(!this.isClockReady())
                return GeneralError.NO_ERROR;
        }

        synchronized (this) {
            for(Task task : this.taskList)
                task.taskReady = false;

            this.clockCycle++;
            this.lastRefresh = System.currentTimeMillis();
        }

        return GeneralError.NO_ERROR;
    }

    /**
     * An implementation of the {@link TaskLoop#init() init()} that will initialize the clock.
     * @return {@link GeneralError#NO_ERROR NO_ERROR}: initialization ran into no errors.
     */
    @Override
    protected synchronized final Error init() {
        this.lastRefresh = System.currentTimeMillis();

        return GeneralError.NO_ERROR;
    }

    /**
     * Adds a {@link Task} that the {@link Clock} will check before proceeding on to the next cycle.
     * @param task An {@link Task) that will be added to the clock.
     * @return  {@link TaskError#TASK_ALREADY_BOUND}: Returned if clock has already been added to Clock. {@link GeneralError#NO_ERROR}: if function exited without any errors.
     */
    protected final Error addTask(Task task) {
        if(task.taskClockID != -1)
            return TaskError.TASK_ALREADY_BOUND;

        task.taskClockID = currentID++;
        this.taskList.add(task);

        return GeneralError.NO_ERROR;
    }

    /**
     * A function that removes a task from the {@link Clock}.
     * @param tasClockID The ID of the {@link Task} that is to be removed from the {@link Clock}.
     * @return {@link TaskError#TASK_NOT_BOUND}: returned if {@link Task} that is attempted to be removed has not been bound to the {@link Clock}. {@link GeneralError#NO_ERROR}: returned if function exited without errors.
     */
    protected final Error removeTask(int tasClockID) {
        int index = this.findTask(tasClockID);
        if(index == -1)
            return TaskError.TASK_NOT_BOUND;

        this.taskList.remove(index);

        return GeneralError.NO_ERROR;
    }

    /**
     * Temporarily removes a {@link Task} from the {@link Clock}.
     * @param taskClockID The id of the {@Link Task} that is to be paused.
     * @return {@link GeneralError#NO_ERROR}: returned if function exits without errors.
     */
    public final Error pauseTask(int taskClockID) {
         int index = this.findTask(taskClockID);
         if(index == -1)
             return GeneralError.NO_ERROR;

         this.taskList.get(index).taskIsPaused = true;

        return GeneralError.NO_ERROR;
    }

    /**
     * Re-adds a {@link Task} that has previously been paused.
     * @param taskClockID The id of the {@link Task} that is to be resumed.
     * @return {@link GeneralError#NO_ERROR}: returned if function exited without errors.
     */
    public final Error resumeTask(int taskClockID) {
        int index = this.findTask(taskClockID);
        if(index == -1)
            return GeneralError.NO_ERROR;

        this.taskList.get(index).taskIsPaused = false;

        return GeneralError.NO_ERROR;
    }

    /**
     * Returns whether the {@link Clock} is ready for the next cycle.
     * @return Returns true if {@link Clock} is ready: returns false if {@link Clock} is not.
     */
    private synchronized boolean isClockReady() {
        for(Task task : this.taskList) {
            if(!task.taskReady && !task.taskIsPaused)
                return false;
        }

        return true;
    }

    /**
     * Returns whether the {@link Clock} is ready for its next cycle.
     * @param cycle The current cycle that the {@link Clock} is on.
     * @return true if next cycle is ready; false if next cycle isn't ready.
     */
    protected final synchronized boolean nextCycleReady(int cycle) {
        return !(cycle < this.clockCycle);
    }

    /**
     * Marks the calling {@link Task} as ready.
     * @param callingTask a reference to the calling object of the {@link Task}.
     * @return {@link TaskError#TASK_NOT_BOUND}: returned if calling {@link Task} isn't bound to the {@link Clock}. {@link GeneralError#NO_ERROR}: issued when function exited without any errors.
     */
    protected final synchronized Error taskReady(Task callingTask) {
        if(callingTask.taskClockID == -1)
            return TaskError.TASK_NOT_BOUND;

        callingTask.taskReady = true;

        return GeneralError.NO_ERROR;
    }

    /**
     * Returns the current cycle that the {@link Clock} is on.
     * @return integer that represents the current cycle of the {@link Clock}.
     */
    public int getCurrentCycle() {
        return this.clockCycle;
    }

    private ArrayList<Task> taskList;
    private int currentID;

    private int clockCycle;
    private double refreshRate;
    private long lastRefresh;
}

//todo add clock monitor (FUTURE CHANGE)
//todo make task list into binary tree.
//todo add function to be overrided when class is extended.