import model.Result;
import scheduler.Scheduler;
import scheduler.impl.FCFSImpl;
import scheduler.impl.NonPreemptiveSJFImpl;
import utils.CSVReader;
import model.Process;
import utils.FileWriterUtil;


import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String inputFile = "processes.csv";
        String outputFile = "output.csv";

        System.out.println("csv okunma burada "+ inputFile);

        //process listesini okuma
        List<Process> processes = CSVReader.readProcessesFromCSV(inputFile);

        System.out.println(" toplam processes " + processes.size());

        // Scheduler sec ve calıstır
        Scheduler scheduler = new FCFSImpl();
        List<Result> results = scheduler.schedule(processes,0);

        System.out.println("\n===== FCFS SJF Scheduling Result =====");
        for (Result r : results) {
            System.out.println(r);
        }

        int contextSwitchCount = scheduler.getContextSwitchCount();
        int maxCompletionTime = scheduler.getMaxCompletionTime(results);
        String algorithmName = scheduler.getAlgorithmName();

        // sonuçları kayıt etme
        FileWriterUtil.writeResultsToFile(results, algorithmName,contextSwitchCount,maxCompletionTime );

        System.out.println("sonuç kayıt edildi"+outputFile);

    }
}