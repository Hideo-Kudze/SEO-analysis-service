package com.HideoKuzeGits.TestTask;

import javax.security.auth.login.Configuration;
import javax.servlet.Servlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by root on 05.11.14.
 */

public class MyThreadPool {

    private Integer maxThreadsInThePool = 50;
    private Integer awaitTerminationMinutes = 20;




    public void  execute(List<Runnable> tasks){

        ExecutorService executorService = Executors.newFixedThreadPool(maxThreadsInThePool);

        for (Runnable task : tasks)
            executorService.execute(task);



        executorService.shutdown();
        try {
            executorService.awaitTermination(awaitTerminationMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public  void  executeWithDelay(List<Runnable> tasks, Integer delay){

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(maxThreadsInThePool);

        for (Runnable task : tasks)
            executorService.schedule(task, delay, TimeUnit.MICROSECONDS);

        executorService.shutdown();
        try {
            executorService.awaitTermination(awaitTerminationMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public<T>  List<T> getResultsWithDelay(List<Callable<T>> tasks)  {

        List<T> results = new ArrayList<T>();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(maxThreadsInThePool);
        List<Future<T>> futureList = new ArrayList<Future<T>>();

        for (Callable<T> task : tasks) {
            Future<T> future = executorService.schedule(task, 1250, TimeUnit.MICROSECONDS);
            futureList.add(future);
        }


        executorService.shutdown();

        for (Future<T> future : futureList)
            try {
                results.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        return results;
    }

    public Integer getMaxThreadsInThePool() {
        return maxThreadsInThePool;
    }

    public void setMaxThreadsInThePool(Integer maxThreadsInThePool) {
        this.maxThreadsInThePool = maxThreadsInThePool;
    }

    public Integer getAwaitTerminationMinutes() {
        return awaitTerminationMinutes;
    }

    public void setAwaitTerminationMinutes(Integer awaitTerminationMinutes) {
        this.awaitTerminationMinutes = awaitTerminationMinutes;
    }

}
