package scheduler.impl;

import model.Process;
import model.Result;
import scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PreemptiveSJFImpl implements Scheduler {

    private int contextSwitchCount = 0;
    private int maxCompletionTime = 0;

    @Override
    public List<Result> schedule(List<Process> initialProcesses, int timeQuantum) {

        List<Result> results = new ArrayList<Result>();
        List<Process> processes = initialProcesses;

        maxCompletionTime = 0;
        contextSwitchCount = 0;

        int currentTime = 0;
        Process currentProcess = null;

        while (!processes.isEmpty()) {
            int finalCurrentTime = currentTime;

            Process next = processes.stream()
                    .filter(p -> p.getArrivalTime() <= finalCurrentTime)
                    .min(Comparator.comparing(Process::getRemainingTime))
                    .orElse(null);

            if (next == null) {
                currentTime++;
                continue;
            }

            if(currentProcess != null && currentProcess != next) {
                contextSwitchCount++;

            }
            currentProcess = next;

            // İlk kez çalışıyorsa firstexecutionTime ayarla
            if(currentProcess.getFirstExecutionTime() == -1)
            {
                currentProcess.setFirstExecutionTime(currentTime);
            }

            //1 birim çalışıyorsa
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() -1);
            currentTime++;

            // Eğer süreç tamamlandıysa
            if (currentProcess.getRemainingTime() == 0) {
                int finishTime = currentTime;
                currentProcess.setCompletionTime(finishTime);

                int turnaroundTime = finishTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                currentProcess.setTurnaroundTime(turnaroundTime);
                currentProcess.setWaitingTime(waitingTime);

                results.add(new Result(200, "OK", currentProcess));
                processes.remove(currentProcess);

                if (finishTime > maxCompletionTime) {
                    maxCompletionTime = finishTime;
                }
                currentProcess = null;
            }
        }

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
        return "PreemptiveSJFImpl";
    }
}
