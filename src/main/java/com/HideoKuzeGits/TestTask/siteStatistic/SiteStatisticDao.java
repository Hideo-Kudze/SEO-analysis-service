package com.HideoKuzeGits.TestTask.siteStatistic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 04.11.14.
 */
@Service
@Transactional
public class SiteStatisticDao {

    @PersistenceContext
    private EntityManager em;

    private Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .setPrettyPrinting()
            .create();

    public boolean exists(String domain) {

        TypedQuery<Long> query = em.createQuery("SELECT COUNT (p) FROM SiteStatisticJsonWrap p" +
                "  WHERE p.domain = :domain", Long.class);
        query.setParameter("domain", domain);
        Long count = query.getSingleResult();

        return count == 1;
    }

    public SiteStatistic getSiteStatistic(String domain) {

        SiteStatisticJsonWrap siteStatisticJsonWrap = em.find(SiteStatisticJsonWrap.class, domain);

        if (siteStatisticJsonWrap == null)
            return null;

        String json = siteStatisticJsonWrap.getJson();
        return gson.fromJson(json, SiteStatistic.class);
    }

    public void saveSiteStatistic(SiteStatistic siteStatistic) {

        if (siteStatistic.getPages().isEmpty())
            return;

        String domain = siteStatistic.getDomain();
        String json = gson.toJson(siteStatistic);

        SiteStatisticJsonWrap siteStatisticJsonWrap = new SiteStatisticJsonWrap();
        siteStatisticJsonWrap.setDomain(domain);
        siteStatisticJsonWrap.setJson(json);
        siteStatisticJsonWrap.setDate(new Date());

        em.merge(siteStatisticJsonWrap);
    }

    public void clearOldStatistic() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date lastRegistrationDate = calendar.getTime();

        Query query = em.createQuery("delete  from SiteStatisticJsonWrap r where r.date < :date");
        query.setParameter("date", lastRegistrationDate);
        query.executeUpdate();
    }
}
