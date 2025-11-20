package org.pricetrack.ThreadPrac;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierL {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2);

        Thread task1 = new Thread(new worker(barrier));
        Thread task2 = new Thread(new worker(barrier));
        task1.start();
        task2.start();

        System.out.println("main thread is running");

    }
}

class worker implements Runnable{

    private final CyclicBarrier barrier;

    worker(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {

        System.out.println("started working");
        try {
            Thread.sleep(200);
            System.out.println("started waiting for another threads to finish");
            barrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

    }
}
