package scheduler.impl;

import model.Process;
import model.Result;
import scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinImpl implements Scheduler {

    private int contextSwitchCount = 0;
    private int maxCompletionTime = 0;

    @Override
    public List<Result> schedule(List<Process> initialProcesses, int timeQuantum) {
        List<Result> results = new ArrayList<>();
        List<Process> processes = new ArrayList<>(initialProcesses);

        int currentTime = 0;
        int contextSwitchCount = 0;
        int maxCompletionTime = 0;

        while (!processes.isEmpty()) {
            boolean anyProcessExecuted = false;

            for (Process p : new ArrayList<>(processes)) {
                if (p.getArrivalTime() <= currentTime) {
                    // ilk kez çalışıyorsa
                    if (p.getFirstExecutionTime() == -1) {
                        p.setFirstExecutionTime(currentTime);
                    }

                    int execTime = Math.min(timeQuantum, p.getRemainingTime());
                    p.setRemainingTime(p.getRemainingTime() - execTime);
                    currentTime += execTime;
                    anyProcessExecuted = true;
                    contextSwitchCount++;

                    // işlem tamamlandıysa
                    if (p.getRemainingTime() == 0) {
                        p.setCompletionTime(currentTime);
                        p.setTurnaroundTime(currentTime - p.getArrivalTime());
                        p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
                        results.add(new Result(200, "OK", p));
                        processes.remove(p);

                        if (currentTime > maxCompletionTime)
                            maxCompletionTime = currentTime;
                    }
                }
            }

            // hiçbir process çalışmadıysa zaman ilerlesin
            if (!anyProcessExecuted) {
                currentTime++;
            }
        }

        this.contextSwitchCount = contextSwitchCount;
        this.maxCompletionTime = maxCompletionTime;

        return results;
    }

    @Override
    public int getContextSwitchCount() {
        return contextSwitchCount;
    }

    @Override
    public int getMaxCompletionTime(List<Result> results) {
        return maxCompletionTime;
    }

    @Override
    public String getAlgorithmName() {
        return "RoundRobin";
    }
}

