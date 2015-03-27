package com.HideoKuzeGits.TestTask.procesSite;

import com.HideoKuzeGits.TestTask.DuplicateContentFinder;
import com.HideoKuzeGits.TestTask.google.pageSpeed.PageSpeedService;
import com.HideoKuzeGits.TestTask.google.search.GoogleSearchApi;
import com.HideoKuzeGits.TestTask.page.Page;
import com.HideoKuzeGits.TestTask.siteStatistic.SiteStatistic;
import com.HideoKuzeGits.TestTask.siteStatistic.SiteStatisticDao;
import com.HideoKuzeGits.TestTask.siteWeight.SiteWeightService;
import com.HideoKuzeGits.TestTask.spider.SpiderService;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by root on 04.11.14.
 */

@Service
public class ProcessSiteService {

    private static Logger log = Logger.getLogger(ProcessSiteService.class.getName());

    @Autowired
    private SiteWeightService siteWeightService;

    @Autowired
    private GoogleSearchApi googleSearchApi;


    @Autowired
    private SpiderService spiderService;

    @Autowired
    private DuplicateContentFinder duplicateFinder;

    @Autowired
    private PageSpeedService pageSpeedService;

    @Autowired
    private SiteStatisticDao siteStatisticDao;



    public void processSite(String domain) {

        log.info("Begin process site" + domain);

        if (siteStatisticDao.exists(domain))
            return;

        SiteStatistic siteStatistic = new SiteStatistic();
        siteStatistic.setDomain(domain);

        try {
            Integer tic = siteWeightService.getTIC(domain);
            siteStatistic.setTic(tic);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        Integer pr = siteWeightService.getPR(domain);
        siteStatistic.setPr(pr);

        List<String> indexedPages = googleSearchApi.getIndexedPages(domain);

        List<Page> pages = spiderService.findPages(indexedPages, domain);

        Collection<Page> nonBrokenPages = Collections2.filter(pages, new Predicate<Page>() {
            @Override
            public boolean apply(Page page) {
                return !Strings.isNullOrEmpty(page.getContent()) && !page.isDuplicate();
            }
        });

        ArrayList<Page> nonBrokenPagesList = new ArrayList<Page>(nonBrokenPages);

//        googleSearchApi.checkIsPageIndexed(nonBrokenPagesList);

        pageSpeedService.processPages(nonBrokenPagesList);

        duplicateFinder.findDuplicateContent(nonBrokenPagesList);

        siteStatistic.setPages(pages);

        siteStatisticDao.saveSiteStatistic(siteStatistic);

    }

}
