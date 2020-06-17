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
public class Threader {

    static ExecutorService service = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        new Threader().start();
        service.shutdown();
    }

    private void start() {
        CompletionService<Void> completionService = new ExecutorCompletionService<Void>(
                service);
        /*
         * Holds all the futures for the submitted tasks
         */
        List<Future<Void>> results = new ArrayList<Future<Void>>();

        for (int i = 0; i < 3; i++) {
            final int callableNumber = i;

            results.add(completionService.submit(new Callable<Void>() {

                     @Override
                     public Void call() throws Exception {
                         System.out.println("Task " + callableNumber
                                 + " in progress");
                         try {
                             Thread.sleep(callableNumber * 100000);
                         } catch (InterruptedException ex) {
                             System.out.println("Task " + callableNumber
                                     + " cancelled");
                             return null;
                         }
                         /*if (callableNumber == 1) {
                             throw new Exception("Wrong answer for task "
                                     + callableNumber);
                         }*/
                         System.out.println("Task " + callableNumber + " complete");
                         return null;
                     }
                 }

            ));
        }

        boolean complete = false;
        while (!complete) {
            complete = true;
            Iterator<Future<Void>> futuresIt = results.iterator();
            while (futuresIt.hasNext()) {
                if (futuresIt.next().isDone()) {
                    futuresIt.remove();
                } else {
                    complete = false;
                }
            }

            if (!results.isEmpty()) {
                try {
                    /*
                     * Examine results of next completed task
                     */
                    completionService.take().get();
                } catch (InterruptedException e) {
                    /*
                     * Give up - interrupted.
                     */
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    /*
                     * The task threw an exception
                     */
                    System.out.println("Execution exception " + e.getMessage());
                    complete = true;
                    for (Future<Void> future : results) {
                        if (!future.isDone()) {
                            System.out.println("Cancelling " + future);
                            future.cancel(true);
                        }
                    }
                }
            }
        }

    }
}