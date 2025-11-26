// java
package utils;

import model.Process;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private static final String CSV_SEPARATOR = ",";

    public static List<Process> readProcessesFromCSV(String filePath) {
        List<Process> processes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // başlığı atla
            br.readLine();
            // her satırı işle oku
            while ((line = br.readLine()) != null) {
                // satırı virgülle ayır
                String[] values = line.split(CSV_SEPARATOR);
                // gerekli değerleri al
                if (values.length >= 4) {
                    try {
                        // değerleri parse et
                        String id = values[0].trim();
                        int arrivalTime = Integer.parseInt(values[1].trim());
                        int burstTime = Integer.parseInt(values[2].trim());
                        int priority = Integer.parseInt(values[3].trim());
                        // yeni process oluştur ve listeye ekle
                        Process process = new Process(id, arrivalTime, burstTime, priority, burstTime);
                        processes.add(process);
                    } catch (NumberFormatException e) {
                        System.err.println("Geçersiz sayı formatı: " + e.getMessage());
                    }
                } else {
                    System.err.println("Yetersiz veri satırı: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return processes;
    }
}