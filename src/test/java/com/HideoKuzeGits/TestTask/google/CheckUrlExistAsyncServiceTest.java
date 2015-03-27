package com.HideoKuzeGits.TestTask.google;

import com.HideoKuzeGits.TestTask.google.search.CheckUrlExistAsyncService;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by root on 05.11.14.
 */
public class CheckUrlExistAsyncServiceTest {
    @Test
    public void testGetExistsUrls() throws Exception {

        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("//home/hideo/IdeaProjects/TestTask/src/main/webapp/WEB-INF/dispatcher-servlet.xml,//home/hideo/IdeaProjects/TestTask/src/main/webapp/WEB-INF/spring_hibernate.xml".split(","));
        CheckUrlExistAsyncService checkUrlExistAsyncService = ctx.getBean(CheckUrlExistAsyncService.class);

        String brokenUrl = "http://jusfsdfnit.soursdfsdfsdfceforge.net/";

        List<String> urls = new ArrayList<String>();
        urls.add("https://ru.wikipedia.org");
        urls.add("http://stackoverflow.com/");
        urls.add("http://stackoverflow.com/");
        urls.add("http://junit.sourceforge.net/");
        urls.add(brokenUrl);

        Collection<String> existsUrls = checkUrlExistAsyncService.getUniqueExistsUrls(urls);

        urls.remove(brokenUrl);

        assertTrue(existsUrls.containsAll(urls));
    }
}
