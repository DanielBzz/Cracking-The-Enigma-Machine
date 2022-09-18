package logic;

public class TasksMade {

    private int data;

    public synchronized int get() {

        return data;
    }

    public synchronized void put(int n) {

        this.data = n;
    }
}
