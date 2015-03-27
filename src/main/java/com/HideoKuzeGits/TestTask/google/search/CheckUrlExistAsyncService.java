package com.HideoKuzeGits.TestTask.google.search;

import com.HideoKuzeGits.TestTask.MyThreadPool;
import com.HideoKuzeGits.TestTask.UsefulStaticMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by root on 05.11.14.
 */

@Service
public class CheckUrlExistAsyncService {

    @Autowired
    private MyThreadPool myThreadPool;



    public Collection<String> getUniqueExistsUrls(Collection<String> urls) {

        final Set<String> checkedUrls = new HashSet<String>();
        ArrayList<Runnable> checkPagesTasks = new ArrayList<Runnable>();

        for (final String url : urls) {

            Runnable checkPagesTask = new Runnable() {
                @Override
                public void run() {
                    boolean urlExists = UsefulStaticMethods.urlExists(url);
                    if (urlExists)
                        checkedUrls.add(url);
                }
            };
            checkPagesTasks.add(checkPagesTask);
        }

        myThreadPool.execute(checkPagesTasks);

        return checkedUrls;
    }

}
