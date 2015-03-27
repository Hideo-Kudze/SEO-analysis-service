package com.HideoKuzeGits.TestTask.siteStatistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by root on 04.11.14.
 */

@Service
@EnableScheduling
public class ScheduledReportCleaner {

    @Autowired
    private SiteStatisticDao siteStatisticDao;

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    private void clearOldReports() {

        siteStatisticDao.clearOldStatistic();
    }
}
