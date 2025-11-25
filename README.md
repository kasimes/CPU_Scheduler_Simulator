🚀 CPU Planlama Algoritmaları Simülasyon Projesi
Bu proje, modern işletim sistemlerinin çekirdek bileşenlerinden biri olan CPU (İşlemci) Planlama Algoritmalarının davranışını ve performansını detaylı olarak analiz etmek amacıyla Java dili kullanılarak geliştirilmiş bir simülasyon aracıdır.

Proje, standart bir süreç setini kullanarak farklı planlama stratejilerinin sistem kaynakları üzerindeki etkilerini ölçer ve kritik performans metriklerini hesaplar.

🎯 Proje Vizyonu ve Kullanım Alanı
Projenin temel vizyonu, teorik olarak bilinen planlama algoritmalarının gerçek dünyadaki çalışma mantığını Gantt Şeması çıktılarıyla görselleştirmek ve objektif metriklerle (Bekleme Süresi, Geri Dönüş Süresi, Verimlilik) karşılaştırmalı analiz yapmaya olanak sağlamaktır.

Kapsanan Planlama Stratejileri
Simülasyon motoru, geniş bir yelpazedeki planlama algoritmalarını desteklemektedir:

FCFS (First Come First Served)

Non-Preemptive SJF (Shortest Job First - Kesintisiz)

Preemptive SJF (Shortest Remaining Time First - Kesintili)

Round Robin (Zaman Dilimli)

Non-Preemptive Priority (Kesintisiz Öncelik)

Preemptive Priority (Kesintili Öncelik)

🛠️ Kurulum ve Çalıştırma Rehberi
1. Giriş Verisi Hazırlığı
   Simülasyonun çalışması için, projenin kök dizininde (README'nin bulunduğu yer) aşağıdaki formatta bir processes.csv dosyası gereklidir:

Kod snippet'i

ProcessID,ArrivalTime,BurstTime,Priority
P1,0,10,3
P2,3,5,1
P3,5,2,2
...
2. Proje Yürütme
   Proje, bir Java Virtual Machine (JVM) üzerinde standart komutlar ile çalıştırılabilir.

Bash

# IDE (IntelliJ/Eclipse) üzerinden Main.java dosyasını çalıştırın.
# VEYA
# Komut satırından derleme ve çalıştırma adımlarını takip edin.
java Main
3. Çıktı Raporları
   Her simülasyon çalıştırmasının ardından, sonuçlar ve detaylı metrik raporları, ilgili algoritmanın adıyla etiketlenmiş ([AlgoritmaAdı]_Sonuclar.txt) bir metin dosyasına yazılacaktır.

📊 Ölçülen Performans Metrikleri
Her algoritma, sistem performansını kapsamlı bir şekilde değerlendirmek için aşağıdaki metrikleri hesaplar:

Zaman Tablosu (Gantt Chart): Süreç yürütme sırasının görselleştirilmiş hali.

Maksimum ve Ortalama Bekleme Süresi (Waiting Time): Süreçlerin kuyrukta harcadığı toplam zaman.

Maksimum ve Ortalama Geri Dönüş Süresi (Turnaround Time): Sürecin sisteme girişinden tamamlanmasına kadar geçen toplam süre.

Ortalama CPU Verimliliği (CPU Utilization): CPU'nun kullanıldığı toplam sürenin yüzdesi.

Toplam Bağlam Değiştirme Sayısı (Context Switches): CPU'nun bir süreçten diğerine kaç kez geçtiği.

Oranlar: Çeşitli performans oranları (T(T/W) gibi).