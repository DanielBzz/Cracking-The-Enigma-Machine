package logic;

import javafx.concurrent.Task;

public class smallMain {

    public static void main(String[] args) {

       // BlockingQueue<Runnable> agentTasks = new LinkedBlockingQueue<>(10);
       // ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, agentTasks);
       // threadPool.prestartAllCoreThreads();
       // Runnable r = () -> System.out.println(Thread.currentThread().getName());

        new Thread(new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                System.out.println("daniel");
                return true;
            }}).start();

//        for (int i = 0; i < 10; i++) {
//            try {
//                agentTasks.put(new Task<>() {
//                    @Override
//                    protected Object call() throws Exception {
//                        System.out.println(Thread.currentThread().getName());
//
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        threadPool.shutdown();
//        try {
//            threadPool.awaitTermination(1,TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}