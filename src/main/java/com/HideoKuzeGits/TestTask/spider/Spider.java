
package com.HideoKuzeGits.TestTask.spider;

import com.HideoKuzeGits.TestTask.page.Page;
import com.google.common.base.Strings;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Created by root on 03.11.14.
 */

public class Spider {

    private static Logger log = Logger.getLogger(Spider.class.getName());

    private String domain;

    private Integer maxResultCount;
    private int maxThreadsInThePool = 50;
    private int awaitTerminationMinutes = 20;

    private final ForkJoinPool forkJoinPool;

    private Set<String> findUrls = Collections.synchronizedSet(new HashSet<String>());
    private List<Page> pages = Collections.synchronizedList(new ArrayList<Page>());
    private int findPages = 0;


    public static void main(String[] args) {
        Spider spider = new Spider("http://9gag.com/", 200, 50, 20);

        ArrayList<Page> pages1 = new ArrayList<Page>();
        pages1.add(new Page("http://9gag.com/"));
        spider.run(pages1);
    }


    public Spider(String domain, Integer maxResultCount, int maxThreadsInThePool, int awaitTerminationMinutes) {
        this.maxResultCount = maxResultCount;
        this.domain = domain;
        this.maxThreadsInThePool = maxThreadsInThePool;
        this.awaitTerminationMinutes = awaitTerminationMinutes;
        forkJoinPool = new ForkJoinPool(maxThreadsInThePool);
    }

    public void run(final List<Page> pages) {


        RecursiveAction recursiveAction = new RecursiveAction() {
            @Override
            protected void compute() {
                findPagesAsync(pages);
            }
        };

        ForkJoinPool forkJoinPool = new ForkJoinPool(maxThreadsInThePool);
        forkJoinPool.invoke(recursiveAction);
        forkJoinPool.shutdown();
        try {
            forkJoinPool.awaitTermination(awaitTerminationMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public List<Page> getPages() {
        return pages;
    }

    private List<Page> findPages(Page page) {


        if (processPage(page))
            if (pages.size() < maxResultCount)
                pages.add(page);
            else
                return null;

        ArrayList<String> links = getNewInternalLinks(page);

        log.info(pages.size() + ")" + page.getUrl() + "\n Thread:" + Thread.currentThread());

        return Page.linksToPages(links);

    }

    private void findPagesAsync(final List<Page> pages) {

        ArrayList<RecursiveTask> tasks = new ArrayList<RecursiveTask>();

        for (final Page page : pages) {

            RecursiveAction recursiveAction = new RecursiveAction() {
                @Override
                protected void compute() {

                    List<Page> newPages = findPages(page);

                    if (newPages == null)
                        return;

                    findPagesAsync(newPages);
                }
            };
            recursiveAction.fork();
        }

        for (RecursiveTask task : tasks)
            task.join();

    }

    private boolean processPage(Page page) {


        String url = page.getUrl();

        log.info("Page begin processed. Found pages " + pages.size() + ".");

        try {

            if (pages.size() >= maxResultCount)
                return false;
            Connection.Response response = Jsoup.connect(url).timeout(3000).execute();
            if (pages.size() >= maxResultCount)
                return false;


            if (isUrlRedirect(response)) {
                page.setHasRedirect(true);
            } else {
                String content = response.body();
                page.setContent(content);
                for (Page processedPage : pages) {
                    String processedPageContent = processedPage.getContent();
                    if (processedPageContent != null) {
                        boolean isContentsIdentical = processedPageContent.equals(content);
                        page.setDuplicate(isContentsIdentical || page.isDuplicate());
                    }
                }
            }
            log.info("Page was processed. Found pages " + pages.size() + ".");
        } catch (UnsupportedMimeTypeException e) {
            return false;
        } catch (IOException e) {
            page.setBroken(true);
            e.printStackTrace();
        }

        if (!Strings.isNullOrEmpty(page.getContent()) && !page.isDuplicate())
            findPages++;
        return true;
    }

    private ArrayList<String> getNewInternalLinks(Page page) {

        String url = page.getUrl();

        ArrayList<String> links = new ArrayList<String>();
        String content = page.getContent();

        if (Strings.isNullOrEmpty(content))
            return new ArrayList<String>();

        Document parse = Jsoup.parse(content, url);
        Elements linkElements = parse.select("a[href]");

        for (Element linkElement : linkElements) {

            String link = linkElement.absUrl("href");

            if (!Strings.isNullOrEmpty(link)
                    && link.contains(domain)
                    && !findUrls.contains(link)) {
                findUrls.add(link);
                links.add(link);
            }
        }

        return links;
    }

    public static boolean isUrlRedirect(Connection.Response response) throws IOException {

        int responseCode = response.statusCode();
        return responseCode == 300
                || responseCode == 301
                || responseCode == 302
                || responseCode == 303
                || responseCode == 307;
    }

}
