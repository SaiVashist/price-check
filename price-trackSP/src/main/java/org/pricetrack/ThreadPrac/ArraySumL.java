package org.pricetrack.ThreadPrac;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class ArraySumL {

    public static void main(String[] args) {

        int size = 1_000_000;
        long[] data = new long[size];
        for (int i = 0; i < size; i++) {
            data[i] = ThreadLocalRandom.current().nextInt(100);
        }
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ArraSumTask task = new ArraSumTask(data,0,data.length);
        long sum = forkJoinPool.invoke(task);


    }

    static class ArraSumTask extends RecursiveTask<Long>{
        private static final int THRESHOLD = 10_000;
        private final long[] arr;
        private final int start;
        private final int end;

        ArraSumTask(long[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        /**
         * @return
         */
        @Override
        protected Long compute() {
            int length  =end -start;
            if(length <= THRESHOLD){
                long sum = 0;
                for(int i = 0 ; i < end ; i++){
                    sum+=arr[i];
                }
            }

            int mid = start + length / 2;
            ArraSumTask left = new ArraSumTask(arr,start,mid);
            ArraSumTask right = new ArraSumTask(arr,mid,end);
            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }
}

