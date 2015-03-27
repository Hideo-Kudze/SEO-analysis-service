package com.HideoKuzeGits.TestTask.google;

import com.HideoKuzeGits.TestTask.UsefulStaticMethods;
import com.HideoKuzeGits.TestTask.google.search.GoogleSearchApi;
import com.HideoKuzeGits.TestTask.page.Page;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
/**
 * Created by root on 05.11.14.
 */
public class GoogleSearchApiTest {


    @Test
    public void testCheckIsPageIndexed() throws Exception {

        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("//home/hideo/IdeaProjects/TestTask/src/main/webapp/WEB-INF/dispatcher-servlet.xml,//home/hideo/IdeaProjects/TestTask/src/main/webapp/WEB-INF/spring_hibernate.xml".split(","));
        GoogleSearchApi googleSearchApi = ctx.getBean(GoogleSearchApi.class);



        String indexedPage1 = "http://stackoverflow.com/";
        String indexedPage2 = "http://9gag.com/";
        String notIndexedPag = "http://kivra.kpi.ua/about_us/%D1%81%D0%BF%D0%B5%D1%86%D1%96%D0%B0%D0%BB%D1%8C%D0%BD%D1%96%D1%81%D1%82%D1%8C-%D1%96%D0%BD%D1%82%D0%B5%D0%BB%D0%B5%D0%BA%D1%82%D1%83%D0%B0%D0%BB%D1%8C%D0%BD%D1%96-%D1%82%D0%B5%D1%85%D0%BD%D0%BE/";

        List<String> urls = new ArrayList<String>();
        urls.add(indexedPage1);
        urls.add(indexedPage2);
        urls.add(notIndexedPag);

        List<Page> pages = Page.linksToPages(urls);

        googleSearchApi.checkIsPageIndexed(pages);

        Map<Object,Page> pagesMap = UsefulStaticMethods.convertToMap(pages, "url");

        assertTrue(pagesMap.get(indexedPage1).isIndexed());
        assertTrue(pagesMap.get(indexedPage2).isIndexed());
        assertFalse(pagesMap.get(notIndexedPag).isIndexed());

    }
}
