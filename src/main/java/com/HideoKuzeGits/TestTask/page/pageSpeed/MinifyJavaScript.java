
package com.HideoKuzeGits.TestTask.page.pageSpeed;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class MinifyJavaScript {

    @Expose
    private String localizedRuleName;
    @Expose
    private Double ruleImpact;
    @Expose
    private List<UrlBlock> urlBlocks = new ArrayList<UrlBlock>();

    /**
     * @return The localizedRuleName
     */
    public String getLocalizedRuleName() {
        return localizedRuleName;
    }

    /**
     * @param localizedRuleName The localizedRuleName
     */
    public void setLocalizedRuleName(String localizedRuleName) {
        this.localizedRuleName = localizedRuleName;
    }

    /**
     * @return The ruleImpact
     */
    public Double getRuleImpact() {
        return ruleImpact;
    }

    /**
     * @param ruleImpact The ruleImpact
     */
    public void setRuleImpact(Double ruleImpact) {
        this.ruleImpact = ruleImpact;
    }

    /**
     * @return The urlBlocks
     */
    public List<UrlBlock> getUrlBlocks() {
        return urlBlocks;
    }

    /**
     * @param urlBlocks The urlBlocks
     */
    public void setUrlBlocks(List<UrlBlock> urlBlocks) {
        this.urlBlocks = urlBlocks;
    }

}
