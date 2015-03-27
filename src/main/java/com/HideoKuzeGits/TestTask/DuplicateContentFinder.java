package com.HideoKuzeGits.TestTask;

import com.HideoKuzeGits.TestTask.page.Page;
import com.HideoKuzeGits.TestTask.page.SetMultimapForStrings;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 04.11.14.
 */

@Service
public class DuplicateContentFinder {


    private static Logger log = Logger.getLogger(DuplicateContentFinder.class.getName());


    public static void main(String[] args) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(10, 0.7f, true);
        linkedHashMap.put(1, "1");
        linkedHashMap.put(2, "2");
        linkedHashMap.put(3, "3");
        linkedHashMap.put(4, "4");
        System.out.println(linkedHashMap);
        linkedHashMap.get(2);
        System.out.println(linkedHashMap);

    }

    public void findDuplicateContent(List<Page> pages) {

        SetMultimapForStrings duplicateContent = new SetMultimapForStrings();
        int i = 0;
        for (Page page : pages) {
            i++;
            log.info("DuplicateContentFinder process " + i + " pages from " + pages.size());
            String content = page.getContent();

            if (Strings.isNullOrEmpty(content))
                continue;

            ArrayList<String> textsOnPage = getTextsInHtml(content);
            String url = page.getUrl();

            for (String text : textsOnPage)
                duplicateContent.put(text, url);

            Map<Object, Page> urlPagesMap = null;
            try {
                urlPagesMap = UsefulStaticMethods.convertToMap(pages, "url");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }


            Set<Map.Entry<String, Set<String>>> textUrlsEntries = duplicateContent.entrySet();

            for (Map.Entry<String, Set<String>> textUrlsEntry : textUrlsEntries) {

                Set<String> urls = textUrlsEntry.getValue();
                String text = textUrlsEntry.getKey();

                if (urls.size() < 2)
                    continue;

                for (String pageUrl : urls) {

                    page = urlPagesMap.get(pageUrl);
                    page.addDuplicateContent(textUrlsEntry);
                }
            }
        }
    }

    private ArrayList<String> getTextsInHtml(String html) {
        ArrayList<String> textsOnPage = new ArrayList<String>();
        Pattern pattern = Pattern.compile("(?<=>)(.*?)(?=<)");
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String textCotextOnPage = matcher.group();
            textCotextOnPage = textCotextOnPage.replaceAll("&nbsp(;?)", " ");
            textCotextOnPage = textCotextOnPage.trim();

            if (textCotextOnPage.length() > 20)
                textsOnPage.add(textCotextOnPage);
        }
        return textsOnPage;
    }
}
