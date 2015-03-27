package com.HideoKuzeGits.TestTask.siteWeight;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by root on 01.11.14.
 */

@Service
public class SiteWeightService {


    public Integer getTIC(String url) throws IOException, SAXException {

        String requestTicUrl = "http://bar-navig.yandex.ru/u?show=32&url=" + url;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document yandexTicResponseXml = builder.parse(requestTicUrl);

        Node tcyNode = yandexTicResponseXml.getElementsByTagName("tcy").item(0);
        String tcyValueString = tcyNode.getAttributes().getNamedItem("value").getNodeValue();
        Integer tcyValue = Integer.valueOf(tcyValueString);

        return tcyValue;

    }

    public Integer getPR(String domain) {

        String result = "";

        JenkinsHash jenkinsHash = new JenkinsHash();
        long hash = jenkinsHash.hash(("info:" + domain).getBytes());

        //Append a 6 in front of the hashing value.
        String url = "http://toolbarqueries.google.com/tbr?client=navclient-auto&hl=en&"
                + "ch=6" + hash + "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:" + domain;

        try {
            URLConnection conn = new URL(url).openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            String input;
            while ((input = br.readLine()) != null) {

                // What Google returned? Example : Rank_1:1:9, PR = 9
                result = input.substring(input.lastIndexOf(":") + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("".equals(result)) {
            return 0;
        } else {
            return Integer.valueOf(result);
        }

    }


}
