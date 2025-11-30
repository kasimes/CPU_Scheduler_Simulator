package scheduler.impl;

import model.Process;
import model.Result;
import scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NonPreemptivePriorityImpl implements Scheduler {

    private int contextSwitchCount = 0;
    private  int maxCompletionTime = 0;
    @Override
    public List<Result> schedule(List<Process> initialProcesses, int timeQuantum) {

        List<Result> results = new ArrayList<>();
        List<Process> processes = initialProcesses;

        //başlangıç degeri
        contextSwitchCount = 0;
        maxCompletionTime = 0;

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        Process lastProcess = null;

        while (!processes.isEmpty()) {

            int finalCurrentTime = currentTime;
            Process next = processes.stream()
                    .filter(p -> p.getArrivalTime() <= finalCurrentTime)
                    .min(Comparator.comparingInt(Process::getPriority))
                    .orElse(null);

            if (next == null) {
                currentTime++;
                continue;
            }

            if (lastProcess != null && lastProcess.getId() != next.getId()) {
                contextSwitchCount++;
            }
            //Hesaplamalar
            next.setFirstExecutionTime(currentTime);

            int finishTime = currentTime + next.getBurstTime();
            next.setCompletionTime(finishTime);

            int turnatoundTime =  finishTime - next.getArrivalTime();
            next.setTurnaroundTime(turnatoundTime);

            int waitingTime = turnatoundTime - next.getBurstTime();
            next.setWaitingTime(waitingTime);

            //maxCompletionTime güncellme
            if(finishTime > maxCompletionTime) { maxCompletionTime = finishTime; }

            results.add(new Result(200,"OK",next));

            currentTime = finishTime;
            lastProcess = next;
            processes.remove(next);


        }
        return results;
    }

    @Override
    public int getContextSwitchCount() {
        return contextSwitchCount;
    }

    @Override
    public int getMaxCompletionTime(List<Result> results) {
        if (maxCompletionTime >0) return maxCompletionTime;

        return results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof  Process)
                .map(p->(Process) p)
                .mapToInt(Process::getCompletionTime)
                .max()
                .orElse(0);


    }

    @Override
    public String getAlgorithmName() {
        return "NonPreemptivePriority";
    }

}
