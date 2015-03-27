package com.HideoKuzeGits.TestTask;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 * Created by root on 05.11.14.
 */
public class MyThreadPoolTest {

    @Test
    public void testGetResults() throws Exception {

        ArrayList<Callable<Long>> callables = new ArrayList<Callable<Long>>();

        for (int i = 0; i < 10; i++) {

            Callable<Long> callable = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return System.currentTimeMillis();
                }
            };
            callables.add(callable);
        }


        List<Long> results = new MyThreadPool().getResultsWithDelay(callables);

        assertEquals(results.size(), 10);

    }
}
