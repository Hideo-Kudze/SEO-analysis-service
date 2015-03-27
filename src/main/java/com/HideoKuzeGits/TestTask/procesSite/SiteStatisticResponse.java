package com.HideoKuzeGits.TestTask.procesSite;

import com.HideoKuzeGits.TestTask.siteStatistic.SiteStatistic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

/**
 * Created by root on 06.11.14.
 */


public class SiteStatisticResponse {

    private Integer status;
    private String message;
    private SiteStatistic siteStatistic;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;

        switch (status) {
            case 0:
                message = "Сай не обрабатавыается и отстуствует в базе данных.</br>" +
                        " Пожалуйста перейдите на стартовую страницу и сделайте запрос на обработку";
                break;

            case 1:
                message = "Сайт найден в базе данных";
                break;

            case 2:
                message = "Сайт на данный момент обрабатывается подождите пожалуйста";
                break;


        }

    }

    public String getMessage() {
        return message;
    }


    public SiteStatistic getSiteStatistic() {
        return siteStatistic;
    }

    public void setSiteStatistic(SiteStatistic siteStatistic) {
        this.siteStatistic = siteStatistic;
    }

    @Override
    public String toString() {

        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(this);

        json = json.replaceAll("\n", "</br>");
        json = json.replaceAll(" ", "&#160;");

        return json;
    }
}
