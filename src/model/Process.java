package model;
/*
işlemcide zamanlama algoritmalarında kullanılacak process yapısı
bütün işlemler buradan oluşturuldu

*/
public class Process {

    //işlem adı
    private  String id;

    //işlem sisteme nezamna geliyor buradan bakılacak
    private int arrivalTime;

    // cpu da kaç birim kaldı
    private int burstTime;

    //öncelik degeri
    private  int priority;

    // preemative algoritmaları için kalan çalışma süresi
    private int remainingTime;

    // işlem ilk kez nezaman çalıştı
    private int firstExecutionTime = -1;

    //işlemin tamamlandıgı zaman
    private int completionTime;

    // çevirm süresi

    private int turnaroundTime;

    private int waitingTime;


    //constructer yapsısı

    public Process(String id, int arrivalTime, int burstTime, int priority, int remainingTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = remainingTime;
    }

    // getter setter methodları

    public String getId(){return id;}
    public int getArrivalTime(){return arrivalTime;}
    public int getBurstTime(){return burstTime;}
    public int getPriority(){return priority;}

    public int getRemainingTime(){return remainingTime;}
    public void setRemainingTime(int remainingTime){this.remainingTime = remainingTime;}

    public int getFirstExecutionTime(){return firstExecutionTime;}
    public void setFirstExecutionTime(int firstExecutionTime){this.firstExecutionTime = firstExecutionTime;}

    public int getCompletionTime(){return completionTime;}
    public void setCompletionTime(int completionTime){this.completionTime = completionTime;}

    public int getTurnaroundTime(){return turnaroundTime;}
    public void setTurnaroundTime(int turnaroundTime) {this.turnaroundTime = turnaroundTime;}

    public  int getWaitingTime(){return waitingTime;}
    public void setWaitingTime(int waitingTime){this.waitingTime = waitingTime;}

    @Override
    public String toString(){
        return id + " (Geliş=" + arrivalTime + ", Süre=" + burstTime + ", Öncelik=" + priority + ")";
    }
}
