package gy.finolo.asyncdemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-17 19:00
 */
public class Threader2 {

    static ExecutorService service = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        new Threader2().start();
        service.shutdown();
    }

    private void start() {
        CompletionService<Void> completionService = new ExecutorCompletionService<Void>(
                service);
        /*
         * Holds all the futures for the submitted tasks
         */
        List<Future<Void>> results = new ArrayList<Future<Void>>();

        for (int i = 0; i < 6; i++) {
            final int callableNumber = i;

            results.add(completionService.submit(new Callable<Void>() {

                                                     @Override
                                                     public Void call() throws Exception {
                                                         System.out.println(System.currentTimeMillis() + " Task " + callableNumber
                                                                 + " starts");
                                                         if (callableNumber == 0) {
                                                             throw new Exception(System.currentTimeMillis() + " Wrong answer for task "
                                                                     + callableNumber);
                                                         }

                                                         try {
                                                             Thread.sleep(callableNumber * 1000 + 1000);
                                                         } catch (InterruptedException ex) {
                                                             System.out.println(System.currentTimeMillis() + " Task " + callableNumber
                                                                     + " cancelled");
                                                             return null;
                                                         }



                                                         System.out.println(System.currentTimeMillis() + " Task " + callableNumber + " complete");
                                                         return null;
                                                     }
                                                 }
            ));
        }

/*        boolean complete = false;
        while (!complete) {
            complete = true;
            Iterator<Future<Void>> futuresIt = results.iterator();
            while (futuresIt.hasNext()) {
                if (futuresIt.next().isDone()) {
                    futuresIt.remove();
                } else {
                    complete = false;
                }
            }*/

        for (int i = 0; i < 6; i++) {
            try {
                /*
                 * Examine results of next completed task
                 */
                System.out.println(System.currentTimeMillis() + " a: " + i);
                Future<Void> take = completionService.take();
                System.out.println(System.currentTimeMillis() + " b: " + i);
//                if (!take.isCancelled()) {
                    take.get();
//                }
                System.out.println(System.currentTimeMillis() + " c: " + i);
            } catch (InterruptedException e) {
                /*
                 * Give up - interrupted.
                 */
                System.out.println(System.currentTimeMillis() + " interrupted");
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                /*
                 * The task threw an exception
                 */
                System.out.println(System.currentTimeMillis() + " Execution exception " + e.getMessage());
//                    complete = true;

                for (Future<Void> future : results) {
                    System.out.println(System.currentTimeMillis() + " here");
                    if (!future.isDone()) {
                        System.out.println(System.currentTimeMillis() + " Cancelling " + future);
                        future.cancel(true);
                    }
                }
                // TODO: 不取值了，队列是否会有问题？
                break;
            }
        }
//        }

    }
}