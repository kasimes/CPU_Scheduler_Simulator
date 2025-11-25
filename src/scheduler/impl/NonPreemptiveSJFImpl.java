
package scheduler.impl;

import model.Process;
import model.Result;
import scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class NonPreemptiveSJFImpl implements Scheduler {

    @Override
    public List<Result> schedule(List<java.lang.Process> initialProcesses, int timeQuantum) {
        List<Process> processes = new ArrayList<>(initialProcesses);
        List<Result> results = new ArrayList<>();

        // Varış zamanına göre sırala
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        // Ready queue: Shortest Job First (burstTime), eşit burst ise varış zamanı
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator
                        .comparingInt(Process::getBurstTime)
                        .thenComparingInt(Process::getArrivalTime)
        );

        int currentTime = 0;
        int processIndex = 0;
        int completedCount = 0;
        String lastProcessId = null;
        int contextSwitchCount = 0;

        while (completedCount < processes.size() || !readyQueue.isEmpty()) {

            // Gelen süreçleri ekle
            while (processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(processIndex));
                processIndex++;
            }

            // CPU boşsa bir sonraki sürecin gelmesini bekle
            if (readyQueue.isEmpty()) {
                if (processIndex < processes.size()) {
                    currentTime = processes.get(processIndex).getArrivalTime();
                    continue;
                } else {
                    break;
                }
            }

            // En kısa işi seç ve kesintisiz çalıştır
            Process currentProcess = readyQueue.poll();

            if (lastProcessId != null && !currentProcess.getId().equals(lastProcessId)) {
                contextSwitchCount++;
            }
            lastProcessId = currentProcess.getId();

            int startTime = currentTime;
            currentProcess.setFirstExecutionTime(startTime);

            currentTime += currentProcess.getBurstTime();
            int completionTime = currentTime;
            currentProcess.setCompletionTime(completionTime);
            completedCount++;

            int turnaroundTime = completionTime - currentProcess.getArrivalTime();
            currentProcess.setTurnaroundTime(turnaroundTime);

            int waitingTime = turnaroundTime - currentProcess.getBurstTime();
            currentProcess.setWaitingTime(waitingTime);

            // Result oluşturma: proje içindeki Result API'sine göre uyarlayın.
            results.add(createResult(currentProcess));
        }

        return results;
    }

    // Projeye göre değiştirin: eğer Result.of(...) ya da farklı bir constructor varsa burayı ona göre değiştirin.
    private Result createResult(Process p) {
        return new Result(200, "OK", p);
    }

    @Override
    public String getAlgorithmName() {
        return "NonPreemptiveSJF";
    }
}
