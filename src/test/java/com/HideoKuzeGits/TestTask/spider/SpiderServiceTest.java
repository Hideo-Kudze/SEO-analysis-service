package com.HideoKuzeGits.TestTask.spider;

import com.HideoKuzeGits.TestTask.page.Page;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
/**
 * Created by root on 04.11.14.
 */
public class SpiderServiceTest {


    @Test
    public void testFindPages() throws Exception {


        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("//home/hideo/IdeaProjects/TestTask/src/main/webapp/WEB-INF/dispatcher-servlet.xml,//home/hideo/IdeaProjects/TestTask/src/main/webapp/WEB-INF/spring_hibernate.xml".split(","));
        SpiderService spiderService = ctx.getBean(SpiderService.class);

        List<String> urls = new ArrayList<String>();
        urls.add("http://caramel.8nio.com/1.html");
        urls.add("http://caramel.8nio.com/2.html");

        String domain = "http://caramel.8nio.com";
        List<Page> pages = spiderService.findPages(urls, domain);

        ArrayList<String> hostedPages = new ArrayList<String>();
        hostedPages.add("http://caramel.8nio.com/1.html");
        hostedPages.add("http://caramel.8nio.com/1.1.html");
        hostedPages.add("http://caramel.8nio.com/1.2.html");
        hostedPages.add("http://caramel.8nio.com/1.3.html");
        hostedPages.add("http://caramel.8nio.com/2.html");
        hostedPages.add("http://caramel.8nio.com/2.1.html");
        hostedPages.add("http://caramel.8nio.com/2.2.html");
        hostedPages.add("http://caramel.8nio.com/2.3.html");

        ArrayList<String> pageUrls = new ArrayList<String>();
        for (Page page : pages)
            pageUrls.add(page.getUrl());

        assertTrue(pageUrls.containsAll(hostedPages));


        System.out.println(pages);
    }
}
