
package com.HideoKuzeGits.TestTask.page.pageSpeed;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class FormattedResults {

    @Expose
    private String locale;
    @Expose
    private RuleResults ruleResults;

    /**
     * @return The locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale The locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return The ruleResults
     */
    public RuleResults getRuleResults() {
        return ruleResults;
    }

    /**
     * @param ruleResults The ruleResults
     */
    public void setRuleResults(RuleResults ruleResults) {
        this.ruleResults = ruleResults;
    }

}
