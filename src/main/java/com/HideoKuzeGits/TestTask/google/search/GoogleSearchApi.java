package com.HideoKuzeGits.TestTask.google.search;

import com.HideoKuzeGits.TestTask.MyThreadPool;
import com.HideoKuzeGits.TestTask.google.captcha.CaptchaNotPasseException;
import com.HideoKuzeGits.TestTask.google.captcha.CaptchaPassService;
import com.HideoKuzeGits.TestTask.page.Page;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by root on 03.11.14.
 */

public class GoogleSearchApi {

    private static Logger log = Logger.getLogger(GoogleSearchApi.class.getName());

    @Autowired
    private MyThreadPool myThreadPool;

    @Autowired
    private CaptchaPassService captchaService;


    private String google = "http://www.google.com/search?q=";
    private String charset = "UTF-8";
    private static String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36";

    private Integer maxResultCount = 250;
    private int retries = 5;
    private int oneTryAwaitMinutes = 5;
    private int delayBetweenRequests = 1000;

    private Map<String, String> cookies = new HashMap<String, String>();

    private static final Lock lock = new ReentrantLock();
    private static AtomicBoolean captchaReached = new AtomicBoolean(false);
    private static CountDownLatch captchaLatch = new CountDownLatch(1);
    private AtomicInteger threadsPassedLatch = new AtomicInteger(1);


    public List<String> getIndexedPages(final String domain) {

        List<Callable<Set<String>>> searchResultsTasks = new ArrayList<Callable<Set<String>>>();

        for (int i = 0; i < 10; i++) {

            final Integer startFromNum = i * 100;
            final int resultsOnPage;

            if (startFromNum >= maxResultCount)
                break;
            else if (startFromNum + 100 > maxResultCount)
                resultsOnPage = maxResultCount % 100;
            else
                resultsOnPage = 100;

            Callable<Set<String>> searchResultsTask = new Callable<Set<String>>() {
                @Override
                public Set<String> call() throws Exception {
                    return getOnePageSearchResultsSafeWrap(domain, startFromNum, resultsOnPage);
                }
            };
            searchResultsTasks.add(searchResultsTask);
        }

        List<Set<String>> searchResults = myThreadPool.getResultsWithDelay(searchResultsTasks);
        HashSet<String> allSearchesResult = new HashSet<String>();

        for (Set<String> searchResult : searchResults) {
            allSearchesResult.addAll(searchResult);
        }

        return new ArrayList<String>(allSearchesResult);

    }

    public void checkIsPageIndexed(Collection<Page> pages) {


        Collection<Page> pagesWithoutIndexedStatus = Collections2.filter(pages, new Predicate<Page>() {
            @Override
            public boolean apply(Page page) {
                return page.isIndexed() == null;
            }
        });

        ArrayList<Runnable> tasks = new ArrayList<Runnable>();


        for (final Page page : pagesWithoutIndexedStatus) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    checkIsPageIndexed(page);
                }
            };
            tasks.add(runnable);
        }

        myThreadPool.execute(tasks);


    }

    private void checkIsPageIndexed(Page page) {

        log.info("Check is page indexed " + page.getUrl());

        String url = page.getUrl();
        Set<String> searchResults = getOnePageSearchResultsSafeWrap(url, 0, 1);

        if (searchResults.isEmpty())
            page.setIndexed(false);
        else {
            String firstResultLink = searchResults.iterator().next();
            boolean indexed = page.getUrl().equals(firstResultLink);
            page.setIndexed(indexed);
        }

    }

    private Set<String> getOnePageSearchResultsSafeWrap(String siteUrL, Integer startFromNum, Integer resultsOnPage) {


        for (int i = 0; i < retries; i++) {


            threadsPassedLatch.decrementAndGet();
            if (captchaReached.get())
                try {
                    captchaLatch.await(oneTryAwaitMinutes, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            threadsPassedLatch.incrementAndGet();

            try {
                return getOnePageSearchResults(siteUrL, startFromNum, resultsOnPage);
            } catch (IOException e) {
                e.printStackTrace();
                if (e instanceof HttpStatusException)
                    if (((HttpStatusException) e).getStatusCode() == 503) {
                        captchaReached.set(true);
                        //if (threadsPassedLatch.get() == 1)
                            try {
                                log.warning("Google search reached captcha.");
                                captchaService.captchaReached();
                            } catch (CaptchaNotPasseException e1) {
                                e1.printStackTrace();
                                captchaReached.set(false);
                                captchaLatch.countDown();
                                captchaLatch = new CountDownLatch(1);
                            }
                    }
            }
        }

        return new HashSet<String>();
    }

    public void captchaPassedCallBack(Map<String, String> cookies) {

        this.cookies = cookies;
        captchaReached.set(false);
        captchaLatch.countDown();
        captchaLatch = new CountDownLatch(1);
        log.info("Captcha was passed successfully.");
    }

    private Set<String> getOnePageSearchResults(String siteUrL, Integer startFromNum, Integer resultsOnPage) throws IOException {

        log.info("Getting google search results starting from: " + startFromNum +
                ", results per page: " + resultsOnPage + ". Url: " + siteUrL);

        Set<String> indexedPages = new HashSet<String>();

        String search = "site:" + siteUrL;

        Elements links;

        String url = google + URLEncoder.encode(search, charset) + "&num=" + resultsOnPage;
        url = url + "&start=" + startFromNum;

        lock.lock();
        try {
            int millis = delayBetweenRequests + new Random().nextInt(delayBetweenRequests / 2);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();

        links = Jsoup.connect(url)
                .cookies(cookies)
                .userAgent(userAgent)
                .timeout(3000)
                .get()
                .select("li.g>div>h3>a");

        for (Element link : links) {
            String pagesUrl = link.absUrl("href");

            if (!pagesUrl.startsWith("http"))
                continue;

            indexedPages.add(pagesUrl);
        }

        return indexedPages;
    }

    public void setMaxResultCount(Integer maxResultCount) {
        this.maxResultCount = maxResultCount;
    }

    public void setDelayBetweenRequests(int delayBetweenRequests) {
        this.delayBetweenRequests = delayBetweenRequests;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public void setOneTryAwaitMinutes(int oneTryAwaitMinutes) {
        this.oneTryAwaitMinutes = oneTryAwaitMinutes;
    }
}
