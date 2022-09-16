package logic;

import javafx.concurrent.Worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class smallMain {

    public static void main(String[] args) {

        BlockingQueue<Runnable> agentTasks = new LinkedBlockingQueue<>(5);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 5, 3, TimeUnit.NANOSECONDS, agentTasks);
        System.out.println(agentTasks.remainingCapacity());

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            try {
                agentTasks.put(() -> {
                        System.out.println(Worker.State.CANCELLED);
                } /*System.out.println(finalI)*/);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int i = 0;
        while (i <= 3){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(agentTasks.remainingCapacity());
            i++;
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1,TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}