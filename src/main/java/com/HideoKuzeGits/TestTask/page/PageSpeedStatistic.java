
package com.HideoKuzeGits.TestTask.page;

import com.HideoKuzeGits.TestTask.page.pageSpeed.FormattedResults;
import com.HideoKuzeGits.TestTask.page.pageSpeed.PageStats;
import com.HideoKuzeGits.TestTask.page.pageSpeed.Version;
import com.google.gson.annotations.Expose;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Random;

@Generated("org.jsonschema2pojo")
public class PageSpeedStatistic {

    @Expose
    private String kind;
    @Expose
    private String id;
    @Expose
    private Integer responseCode;
    @Expose
    private String title;
    @Expose
    private Integer score;
    @Expose
    private PageStats pageStats;
    @Expose
    private FormattedResults formattedResults;
    @Expose
    private Version version;

    /**
     * @return The kind
     */




    public String getKind() {
        return kind;
    }

    /**
     * @param kind The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The responseCode
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode The responseCode
     */
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score The score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return The pageStats
     */
    public PageStats getPageStats() {
        return pageStats;
    }

    /**
     * @param pageStats The pageStats
     */
    public void setPageStats(PageStats pageStats) {
        this.pageStats = pageStats;
    }

    /**
     * @return The formattedResults
     */
    public FormattedResults getFormattedResults() {
        return formattedResults;
    }

    /**
     * @param formattedResults The formattedResults
     */
    public void setFormattedResults(FormattedResults formattedResults) {
        this.formattedResults = formattedResults;
    }

    /**
     * @return The version
     */
    public Version getVersion() {
        return version;
    }

    /**
     * @param version The version
     */
    public void setVersion(Version version) {
        this.version = version;
    }

}
