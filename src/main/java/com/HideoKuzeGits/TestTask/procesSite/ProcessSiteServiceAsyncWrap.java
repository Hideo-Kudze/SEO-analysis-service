package com.HideoKuzeGits.TestTask.procesSite;

import com.HideoKuzeGits.TestTask.siteStatistic.SiteStatistic;
import com.HideoKuzeGits.TestTask.siteStatistic.SiteStatisticDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by root on 06.11.14.
 */

public class ProcessSiteServiceAsyncWrap {

    private static Logger log = Logger.getLogger(ProcessSiteServiceAsyncWrap.class.getName());

    private int domainProcessedAtTheSameTime = 5;
    private int domainProcessed = 7;

    @Autowired
    private ProcessSiteService processSiteService;

    @Autowired
    private SiteStatisticDao siteStatisticDao;

    private final List<String> damianInProgress = new ArrayList<String>();
    ExecutorService threadPool = Executors.newFixedThreadPool(domainProcessedAtTheSameTime);

    public SiteStatisticResponse getSiteStatisticResponse(String domain){

        //Для предотврощение логической гонки
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SiteStatisticResponse siteStatisticResponse = new SiteStatisticResponse();

        if (damianInProgress.contains(domain))
            siteStatisticResponse.setStatus(2);

        else if (siteStatisticDao.exists(domain)){
            SiteStatistic siteStatistic = siteStatisticDao.getSiteStatistic(domain);
            siteStatisticResponse.setSiteStatistic(siteStatistic);
            siteStatisticResponse.setStatus(1);
        }

        else {
            siteStatisticResponse.setStatus(0);
        }

        log.info("Site response for domain:" + domain
                +". Status:" + siteStatisticResponse.getStatus()
                + " Message:" + siteStatisticResponse.getMessage());

        return siteStatisticResponse;
    }

    public void processSite(final String domain) throws ToMachRequestsForSiteProcessingException {


        log.info("Async process site with domain: " + domain
                + "Now site processed: " + damianInProgress);

        if (damianInProgress.contains(domain)){
            return;}

        if (damianInProgress.size() > domainProcessed)
            throw new ToMachRequestsForSiteProcessingException("Can`t process domain. Service overloaded.");

        if (siteStatisticDao.exists(domain))
            return;

        damianInProgress.add(domain);

        Runnable processSiteThread = new Runnable() {
            @Override
            public void run() {
                try{
                processSiteService.processSite(domain);}
                finally {
                    damianInProgress.remove(domain);
                }

            }
        };
        threadPool.execute(processSiteThread);
    }

    public void setDomainProcessed(int domainProcessed) {
        this.domainProcessed = domainProcessed;
    }

    public void setDomainProcessedAtTheSameTime(int domainProcessedAtTheSameTime) {
        this.domainProcessedAtTheSameTime = domainProcessedAtTheSameTime;
    }
}
