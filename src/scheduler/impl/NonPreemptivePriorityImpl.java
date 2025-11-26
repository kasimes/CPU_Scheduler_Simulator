package scheduler.impl;

import model.Process;
import model.Result;
import scheduler.Scheduler;

import java.util.List;

public class NonPreemptivePriorityImpl implements Scheduler {

    @Override
    public List<Result> schedule(List<Process> initialProcesses, int timeQuantum) {
        return List.of();
    }

    @Override
    public int getContextSwitchCount() {
        return 0;
    }

    @Override
    public int getMaxCompletionTime(List<Result> results) {
        return 0;
    }

    @Override
    public String getAlgorithmName() {
        return null;
    }
}
