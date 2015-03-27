package com.HideoKuzeGits.TestTask.siteStatistic;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 04.11.14.
 */
@Entity
public class SiteStatisticJsonWrap {

    @Id
    private String domain;
    @Lob
    private String json;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    public SiteStatisticJsonWrap() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
