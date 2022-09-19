package logic;

public class TasksMadeData {

    private int data = 1;

    public synchronized int get() {

        return data;
    }

    public synchronized void update() {

        data++;
    }
}
