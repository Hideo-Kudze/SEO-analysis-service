
package com.HideoKuzeGits.TestTask.page.pageSpeed;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Version {

    @Expose
    private Integer major;
    @Expose
    private Integer minor;

    /**
     * @return The major
     */
    public Integer getMajor() {
        return major;
    }

    /**
     * @param major The major
     */
    public void setMajor(Integer major) {
        this.major = major;
    }

    /**
     * @return The minor
     */
    public Integer getMinor() {
        return minor;
    }

    /**
     * @param minor The minor
     */
    public void setMinor(Integer minor) {
        this.minor = minor;
    }

}
