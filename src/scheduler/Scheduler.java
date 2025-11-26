package scheduler;

import model.Result;
import model.Process;

import java.util.List;

public interface Scheduler {

    List<Result> schedule(List<Process> initialProcesses, int timeQuantum);

    int getContextSwitchCount();

    int getMaxCompletionTime(List<Result> results);

    String getAlgorithmName();

}
