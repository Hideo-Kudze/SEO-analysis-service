package com.HideoKuzeGits.TestTask.siteStatistic;

import com.HideoKuzeGits.TestTask.page.Page;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 04.11.14.
 */

public class SiteStatistic {

    private String domain;
    private Integer tic;
    private Integer pr;
    private List<Page> pages = new ArrayList<Page>();

    public Integer getTic() {
        return tic;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setTic(Integer tic) {
        this.tic = tic;
    }

    public Integer getPr() {
        return pr;
    }

    public void setPr(Integer pr) {
        this.pr = pr;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {

        String siteStatisticString = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setPrettyPrinting()
                .create()
                .toJson(this);

        siteStatisticString = siteStatisticString.replaceAll(" ", "&#160");
        siteStatisticString = siteStatisticString.replaceAll("\n", "</br>");

        return siteStatisticString;
    }
}
