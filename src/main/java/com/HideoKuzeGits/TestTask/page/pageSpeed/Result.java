
package com.HideoKuzeGits.TestTask.page.pageSpeed;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Result {

    @Expose
    private String format;
    @Expose
    private List<Arg_> args = new ArrayList<Arg_>();

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
    public List<Arg_> getArgs() {
        return args;
    }

    /**
     * @param args The args
     */
    public void setArgs(List<Arg_> args) {
        this.args = args;
    }

}
