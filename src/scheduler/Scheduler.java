package scheduler;

import model.Result;
import model.Process;

import java.util.List;

public interface Scheduler {

    List<Result> schedule(List<java.lang.Process> initialProcesses, int timeQuantum);

    String getAlgorithmName();
}
