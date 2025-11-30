import model.Result;
import scheduler.Scheduler;
import scheduler.impl.*;
import utils.CSVReader;
import model.Process;
import utils.FileWriterUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String inputFile = "processes.csv";

        System.out.println("CSV okunuyor: " + inputFile);

        // Process listesini oku
        List<Process> processes = CSVReader.readProcessesFromCSV(inputFile);
        System.out.println("Toplam process: " + processes.size());

        // Algoritmaların listesi
        List<Scheduler> algorithms = List.of(
                new FCFSImpl(),
                new NonPreemptivePriorityImpl(),
                new PreemptivePriorityImpl(),
                new NonPreemptiveSJFImpl(),
                new PreemptiveSJFImpl(),
                new RoundRobinImpl()
        );

        int timeQuantum = 4; // Round Robin için kullan, diğer algoritmalar ihmal eder

        for (Scheduler scheduler : algorithms) {
            // Process listesinin kopyasını al
            List<Process> processCopy = deepCopyProcesses(processes);

            // Algoritmayı çalıştır
            List<Result> results = scheduler.schedule(processCopy, timeQuantum);

            // Metrikleri al
            int contextSwitchCount = scheduler.getContextSwitchCount();
            int maxCompletionTime = scheduler.getMaxCompletionTime(results);
            String algorithmName = scheduler.getAlgorithmName();

            // Sonuçları dosyaya yaz
            FileWriterUtil.writeResultsToFile(results, algorithmName, contextSwitchCount, maxCompletionTime);
        }

        System.out.println("Tüm algoritmalar çalıştırıldı ve sonuçlar kaydedildi.");
    }

    // Process listesini kopyalamak için helper metot
    private static List<Process> deepCopyProcesses(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority(), p.getBurstTime()));
        }
        return copy;
    }
}
