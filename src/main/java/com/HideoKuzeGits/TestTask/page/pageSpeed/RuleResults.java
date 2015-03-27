
package com.HideoKuzeGits.TestTask.page.pageSpeed;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class RuleResults {

    @Expose
    private com.HideoKuzeGits.TestTask.page.pageSpeed.AvoidBadRequests AvoidBadRequests;
    @Expose
    private com.HideoKuzeGits.TestTask.page.pageSpeed.MinifyJavaScript MinifyJavaScript;
    @Expose
    private com.HideoKuzeGits.TestTask.page.pageSpeed.SpriteImages SpriteImages;

    /**
     * @return The AvoidBadRequests
     */
    public com.HideoKuzeGits.TestTask.page.pageSpeed.AvoidBadRequests getAvoidBadRequests() {
        return AvoidBadRequests;
    }

    /**
     * @param AvoidBadRequests The AvoidBadRequests
     */
    public void setAvoidBadRequests(com.HideoKuzeGits.TestTask.page.pageSpeed.AvoidBadRequests AvoidBadRequests) {
        this.AvoidBadRequests = AvoidBadRequests;
    }

    /**
     * @return The MinifyJavaScript
     */
    public com.HideoKuzeGits.TestTask.page.pageSpeed.MinifyJavaScript getMinifyJavaScript() {
        return MinifyJavaScript;
    }

    /**
     * @param MinifyJavaScript The MinifyJavaScript
     */
    public void setMinifyJavaScript(com.HideoKuzeGits.TestTask.page.pageSpeed.MinifyJavaScript MinifyJavaScript) {
        this.MinifyJavaScript = MinifyJavaScript;
    }

    /**
     * @return The SpriteImages
     */
    public com.HideoKuzeGits.TestTask.page.pageSpeed.SpriteImages getSpriteImages() {
        return SpriteImages;
    }

    /**
     * @param SpriteImages The SpriteImages
     */
    public void setSpriteImages(com.HideoKuzeGits.TestTask.page.pageSpeed.SpriteImages SpriteImages) {
        this.SpriteImages = SpriteImages;
    }

}
