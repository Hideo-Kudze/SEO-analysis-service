package com.HideoKuzeGits.TestTask.google.pageSpeed;

import com.HideoKuzeGits.TestTask.MyThreadPool;
import com.HideoKuzeGits.TestTask.page.Page;
import com.HideoKuzeGits.TestTask.page.PageSpeedStatistic;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by root on 04.11.14.
 */
public class PageSpeedService extends Thread {

    public static final String PAGE_SPEED_AOI_URL = "https://www.googleapis.com/pagespeedonline/v1/runPagespeed";
    public static final String GOOGLE_API_TOKEN = "AIzaSyBgQ7Ij7Bku8qzV5H4GTyyVYqCgBcQ-X1M";
    private static Logger log = Logger.getLogger(PageSpeedService.class.getName());

    private static final Lock lock = new ReentrantLock();
    private int delayBetweenRequests = 1000;
    private int retries = 5;

    @Autowired
    private MyThreadPool myThreadPool;

    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("postgres://qctwiycgouttmf:_7BBRmgCi_dTOJOibl2Q7v0oO_@ec2-54-83-205-46.compute-1.amazonaws.com:5432/d67spn643m0cop");
        int port = uri.getPort();
        System.out.println(port);
    }

    public PageSpeedService() {
    }

    public void  processPages(List<Page> pages) {

        ArrayList<Runnable> setPageSpeedTasks = new ArrayList<Runnable>();

        for (final Page page : pages) {

            Runnable setPageSpeedStatisticTask = new Runnable() {
                @Override
                public void run() {
                    String url = page.getUrl();
                    PageSpeedStatistic pageSpeedStatistic = getOnePageSearchResultsSafeWrap(url);
                    page.setPageSpeedStatistic(pageSpeedStatistic);
                }
            };
            setPageSpeedTasks.add(setPageSpeedStatisticTask);
        };



        myThreadPool.execute(setPageSpeedTasks);
    }



    public PageSpeedStatistic getOnePageSearchResultsSafeWrap(String url) {

        for (int i = 0; i < retries; i++) {
            try {
                return getPageSpeedStatistic(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public PageSpeedStatistic getPageSpeedStatistic(String url) throws IOException {

        log.info("Add page speed statistic to url: " + url);

        String queryUrlString = PAGE_SPEED_AOI_URL + "?key=" + GOOGLE_API_TOKEN + "&url=" + url;

        lock.lock();
        try {
            int millis = delayBetweenRequests + new Random().nextInt(delayBetweenRequests / 2);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();

        URL queryUrl = new URL(queryUrlString);
        InputStream in = queryUrl.openConnection().getInputStream();
        String responseJson = IOUtils.toString(in);
        PageSpeedStatistic pageSpeedStatistic
                = new Gson().fromJson(responseJson, PageSpeedStatistic.class);
        return pageSpeedStatistic;

    }


    public void setRetries(int retries) {
        this.retries = retries;
    }

    public void setDelayBetweenRequests(int delayBetweenRequests) {
        this.delayBetweenRequests = delayBetweenRequests;
    }
}
