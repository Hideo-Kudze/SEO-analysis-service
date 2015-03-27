
package com.HideoKuzeGits.TestTask.page.pageSpeed;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Header {

    @Expose
    private String format;
    @Expose
    private List<Arg> args = new ArrayList<Arg>();

    /**
     * @return The format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format The format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return The args
     */
    public List<Arg> getArgs() {
        return args;
    }

    /**
     * @param args The args
     */
    public void setArgs(List<Arg> args) {
        this.args = args;
    }

}
