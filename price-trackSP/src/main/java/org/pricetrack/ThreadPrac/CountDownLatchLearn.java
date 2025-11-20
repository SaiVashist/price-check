package org.pricetrack.ThreadPrac;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchLearn {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        Thread task1 = new Thread(new ThreadTask1(countDownLatch));
        Thread task2 = new Thread(new ThreadTask2(countDownLatch));
        task1.start();
        task2.start();
        countDownLatch.await();
        System.out.println("All tasks completed main is proceeding");

    }
}


class ThreadTask1 implements Runnable{

private final CountDownLatch latch;

    ThreadTask1(CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     *
     */
    @Override
    public void run() {
        System.out.println("Task one started");

        try {

            for(int i = 0 ; i < 3 ; i++){
                Thread.sleep(200);
                System.out.println("Task one finished" + i);
                latch.countDown();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
          //  latch.countDown();
        }

    }
}

class ThreadTask2 implements Runnable {

    private final CountDownLatch latch;

    ThreadTask2(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Task two started");

        try {
            Thread.sleep(400);
            System.out.println("Task2 finished working");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            latch.countDown();
        }



    }
}
