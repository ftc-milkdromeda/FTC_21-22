package Framework.Tasks;

import Framework.Error;
import Framework.GeneralError;

import java.util.ArrayList;

public class Clock extends TaskLoop{
    public Clock(double refreshRate) {
        this.refreshRate = refreshRate;
        this.taskList = new ArrayList<Task>();
        this.clockCycle = 0;
    }

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

    @Override
    public Error loop() {
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

    @Override
    protected synchronized Error init() {
        this.lastRefresh = System.currentTimeMillis();

        return GeneralError.NO_ERROR;
    }

    protected final Error addTask(Task task) {
        if(task.taskClockID != -1)
            return TaskError.TASK_ALREADY_BOUND;

        task.taskClockID = currentID++;
        this.taskList.add(task);

        return GeneralError.NO_ERROR;
    }
    protected final Error removeTask(int tasClockID) {
        int index = this.findTask(tasClockID);
        if(index == -1)
            return TaskError.TASK_NOT_BOUND;

        this.taskList.remove(index);

        return GeneralError.NO_ERROR;
    }

    public final Error pauseTask(int taskClockID) {
         int index = this.findTask(taskClockID);
         if(index == -1)
             return GeneralError.NO_ERROR;

         this.taskList.get(index).taskIsPaused = true;

        return GeneralError.NO_ERROR;
    }
    public final Error resumeTask(int taskClockID) {
        int index = this.findTask(taskClockID);
        if(index == -1)
            return GeneralError.NO_ERROR;

        this.taskList.get(index).taskIsPaused = false;

        return GeneralError.NO_ERROR;
    }

    private synchronized boolean isClockReady() {
        for(Task task : this.taskList) {
            if(!task.taskReady && !task.taskIsPaused)
                return false;
        }

        return true;
    }
    protected final synchronized boolean nextCycleReady(int cycle) {
        return !(cycle < this.clockCycle);
    }
    protected final synchronized Error taskReady(Task callingTask) {
        if(callingTask.taskClockID == -1)
            return TaskError.TASK_NOT_BOUND;

        callingTask.taskReady = true;

        return GeneralError.NO_ERROR;
    }

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
