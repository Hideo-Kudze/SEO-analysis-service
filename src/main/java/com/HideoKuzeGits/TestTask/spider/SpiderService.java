package com.HideoKuzeGits.TestTask.spider;

import com.HideoKuzeGits.TestTask.page.Page;

import java.util.List;

/**
 * Created by root on 03.11.14.
 */

public class SpiderService {


    private Integer maxResultCount = 250;
    private int maxThreadsInThePool = 50;
    private int awaitTerminationMinutes = 20;

    public List<Page> findPages(List<String> indexedPages, String domain) {

        Spider spider = new Spider(domain, maxResultCount, maxThreadsInThePool, awaitTerminationMinutes);
        List<Page> pages = Page.linksToPages(indexedPages);
        spider.run(pages);
        return spider.getPages();
    }

    public void setMaxResultCount(Integer maxResultCount) {
        this.maxResultCount = maxResultCount;
    }

    public void setMaxThreadsInThePool(int maxThreadsInThePool) {
        this.maxThreadsInThePool = maxThreadsInThePool;
    }

    public void setAwaitTerminationMinutes(int awaitTerminationMinutes) {
        this.awaitTerminationMinutes = awaitTerminationMinutes;
    }
}
