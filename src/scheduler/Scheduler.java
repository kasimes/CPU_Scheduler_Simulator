package scheduler;

import model.Result;

import java.util.List;

public interface Scheduler {

    Result run(List<Result> processes);
}
