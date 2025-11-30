package scheduler.impl;

import model.Process;
import model.Result;
import scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCFSImpl implements Scheduler {

    private int contextSwitchCount;

    @Override
    public List<Result> schedule(List<Process> initialProcesses, int timeQuantum) {

        List<Result> results = new ArrayList<Result>();
        List<Process> processes = initialProcesses;
        contextSwitchCount = 0;

        //varış zamanına gore sıralama
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        String lastProcessId = null;

        for(Process process : processes) {
            if(currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();//CPU ıdle ekleme

            }

            //context switch
            if(lastProcessId != null && !lastProcessId.equals(process.getId())) {
                contextSwitchCount++;

            }
            lastProcessId = process.getId();

            //execution times
            process.setFirstExecutionTime(currentTime);
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);

            //turnaraund times
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());

            //sonuçları listele
            results.add(new Result(200,"OK",process));
        }
        return results;
    }

    @Override
    public int getContextSwitchCount() {
        return contextSwitchCount;
    }

    @Override
    public int getMaxCompletionTime(List<Result> results) {
        return results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof Process)
                .map(p -> (Process) p)
                .mapToInt(Process::getCompletionTime)
                .max()
                .orElse(0);
    }
    @Override
    public String getAlgorithmName() {
        return "FCFS";
    }
}
