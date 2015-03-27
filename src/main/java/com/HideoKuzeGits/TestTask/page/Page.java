package com.HideoKuzeGits.TestTask.page;

import com.sun.org.glassfish.external.statistics.Statistic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.text.ChangedCharSetException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 03.11.14.
 */
public class Page {

    private String url;
    private transient String content;
    private SetMultimapForStrings duplicateContent = new SetMultimapForStrings();
    private Boolean indexed;
    private boolean containsMetaDescription;
    private boolean containsMetaKeyword;
    private PageSpeedStatistic pageSpeedStatistic;
    private boolean hasRedirect;
    private boolean broken;
    private boolean duplicate;


    public Page(String url) {
        this.url = url;
    }

    public static List<Page> linksToPages(List<String> urls){

        ArrayList<Page> pages = new ArrayList<Page>();

        for (String url : urls)
            pages.add(new Page(url));

        return pages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {

        this.content = content;

        Document document = Jsoup.parse(content);

        Elements keywordsMetaElements = document.select("meta[name = keywords]");
        containsMetaKeyword = keywordsMetaElements.size() != 0;

        Elements descriptionMetaElements = document.select("meta[name = description]");
        containsMetaDescription = descriptionMetaElements.size() != 0;

    }

    public Boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public boolean isContainsMetaDescription() {
        return containsMetaDescription;
    }

    public boolean isContainsMetaKeyword() {
        return containsMetaKeyword;
    }

    public PageSpeedStatistic getPageSpeedStatistic() {
        return pageSpeedStatistic;
    }

    public void setPageSpeedStatistic(PageSpeedStatistic pageSpeedStatistic) {
        this.pageSpeedStatistic = pageSpeedStatistic;
    }

    public boolean isHasRedirect() {
        return hasRedirect;
    }

    public void setHasRedirect(boolean isRedirect) {
        this.hasRedirect = isRedirect;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean isBroken) {
        this.broken = isBroken;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean isDuplicate) {
        this.duplicate = isDuplicate;
    }

    public SetMultimapForStrings getDuplicateContent() {
        return duplicateContent;
    }

    public void setDuplicateContent(SetMultimapForStrings duplicateContent) {
        this.duplicateContent = duplicateContent;
    }

    public void addDuplicateContent(Map.Entry<String, Set<String>> duplicateContentEntry) {

        String key = duplicateContentEntry.getKey();
        Set<String> value = duplicateContentEntry.getValue();

        duplicateContent.put(key, value);
    }
}
