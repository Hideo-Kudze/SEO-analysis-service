
package com.HideoKuzeGits.TestTask.page.pageSpeed;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class SpriteImages {

    @Expose
    private String localizedRuleName;
    @Expose
    private Double ruleImpact;

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

}
