package utils;

import model.Process;
import model.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;



public class FileWriterUtil {

    private static  final double CONTEXT_SWITCH_TIME = 0.001;

    public static void writeResultsToFile(
            List<Result> results,
            String algorithmName,
            int contextSwitchCount,
            int maxTime

    ){
        String fileName = getNextFileName(algorithmName +"_",".txt");
            try(FileWriter writer = new FileWriter(fileName)) {
                writer.write("==================================================\n");
                writer.write("           ALGORITMA SONUCLARI: " + algorithmName + "\n");
                writer.write("==================================================\n\n");

                // a) Zaman Tablosu (Gantt Chart)
                writer.write("a) Zaman Tablosu (Gantt Chart):\n");
                writer.write(generateGanttChart(results) + "\n");

                // Performans Metriklerini Hesapla
                double avgWaitingTime = calculateAverageWaitingTime(results);
                double avgTurnaroundTime = calculateAverageTurnaroundTime(results);

                // b) Maksimum ve Ortalama Bekleme Süresi (Waiting Time)
                writer.write("b) Bekleme Süresi (Waiting Time):\n");
                writer.write(String.format("   - Maksimum Bekleme Süresi: %d\n", calculateMaximumWaitingTime(results)));
                writer.write(String.format("   - Ortalama Bekleme Süresi: %.3f\n\n", avgWaitingTime));

                // c) Maksimum ve Ortalama Tamamlanma (Geri Dönüş) Süresi (Turnaround Time)
                writer.write("c) Tamamlanma/Geri Dönüş Süresi (Turnaround Time):\n");
                writer.write(String.format("   - Maksimum Geri Dönüş Süresi: %d\n", calculateMaximumTurnaroundTime(results)));
                writer.write(String.format("   - Ortalama Geri Dönüş Süresi: %.3f\n\n", avgTurnaroundTime));

                // d) Oranlar (T(T/W) gibi)
                writer.write("d) Oran Hesaplamaları:\n");
                writer.write(String.format("   - T(Turnaround/Waiting) Oranı (T/W): %.3f\n\n", (avgWaitingTime == 0 ? Double.POSITIVE_INFINITY : avgTurnaroundTime / avgWaitingTime)));
                // Not: İkinci bir oran (T(T/R)) ödevde net belirtilmemiş, bu yüzden sadece birincisi hesaplanmıştır.

                // e) Ortalama CPU Verimliliği
                writer.write("e) Ortalama CPU Verimliliği:\n");
                writer.write(String.format("   - Toplam Simülasyon Süresi (Max Time): %d\n", maxTime));
                writer.write(String.format("   - Ortalama CPU Verimliliği: %.3f%%\n\n", calculateCPUUtilization(results, maxTime, contextSwitchCount)));

                // f) Toplam Bağlam Değiştirme Sayısı
                writer.write("f) Toplam Bağlam Değiştirme Sayısı:\n");
                writer.write(String.format("   - Toplam Bağlam Değiştirme Sayısı: %d\n\n", contextSwitchCount));

                writer.write("==================================================\n");
                System.out.println("Sonuçlar " + fileName + " dosyasına başarıyla yazıldı.");

            }catch (IOException e) {
                System.err.println("Dosya yazma hatası: " + e.getMessage());
                e.printStackTrace();
            }
    }

    private static String generateGanttChart(List<Result> results) {
        StringBuilder sb = new StringBuilder();
        results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof model.Process)
                .map(p -> (model.Process) p)
                .filter(pr -> pr.getFirstExecutionTime() >= 0 && pr.getCompletionTime() >= pr.getFirstExecutionTime())
                .sorted(Comparator.comparingInt(Process::getFirstExecutionTime))
                .forEach(pr -> {
                    sb.append(String.format("P%s[%d-%d] ", pr.getId(), pr.getFirstExecutionTime(), pr.getCompletionTime()));
                });
        return sb.length() == 0 ? "Gantt verisi yok" : sb.toString().trim();
    }


    private static double calculateAverageWaitingTime(List<Result> results) {
        OptionalDouble avg = results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof Process)
                .map(p -> (Process) p)
                .mapToDouble(Process::getWaitingTime)
                .average();
        return avg.isPresent() ? avg.getAsDouble() : 0.0;
    }

    private static double calculateAverageTurnaroundTime(List<Result> results) {
        OptionalDouble avg = results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof Process)
                .map(p -> (Process) p)
                .mapToDouble(Process::getTurnaroundTime)
                .average();
        return avg.isPresent() ? avg.getAsDouble() : 0.0;
    }

    private static int calculateMaximumWaitingTime(List<Result> results) {
        return results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof Process)
                .map(p -> (Process) p)
                .mapToInt(Process::getWaitingTime)
                .max()
                .orElse(0);
    }

    private static int calculateMaximumTurnaroundTime(List<Result> results) {
        return results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof Process)
                .map(p -> (Process) p)
                .mapToInt(Process::getTurnaroundTime)
                .max()
                .orElse(0);
    }

    // CPU Utilization: toplam iş zamanı (completion - firstExecution) eksi bağlam değişim toplam süresi, % olarak döner
    private static double calculateCPUUtilization(List<Result> results, int maxTime, int contextSwitchCount) {
        double busyTime = results.stream()
                .map(Result::getPayload)
                .filter(p -> p instanceof Process)
                .map(p -> (Process) p)
                .filter(pr -> pr.getFirstExecutionTime() >= 0 && pr.getCompletionTime() >= pr.getFirstExecutionTime())
                .mapToDouble(pr -> pr.getCompletionTime() - pr.getFirstExecutionTime())
                .sum();

        double overhead = contextSwitchCount * CONTEXT_SWITCH_TIME;
        double effectiveBusy = Math.max(0.0, busyTime - overhead);

        if (maxTime <= 0) return 0.0;
        double utilization = (effectiveBusy / maxTime) * 100.0;
        if (utilization < 0) utilization = 0.0;
        if (utilization > 100.0) utilization = 100.0;
        return utilization;
    }

    private static String getNextFileName(String prefix, String extension) {
        File folder = new File("outputs");
        if (!folder.exists()) folder.mkdirs();

        File[] files = folder.listFiles((dir, name) -> name.startsWith(prefix) && name.endsWith(extension));

        int max = 0;
        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(prefix, "").replace(extension, "");
                try {
                    int num = Integer.parseInt(name);
                    if (num > max) max = num;
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }

        return new File(folder, prefix + (max + 1) + extension).getAbsolutePath();


    }
}
