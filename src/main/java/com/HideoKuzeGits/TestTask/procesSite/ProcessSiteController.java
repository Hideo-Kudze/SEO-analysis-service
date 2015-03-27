package com.HideoKuzeGits.TestTask.procesSite;

import com.HideoKuzeGits.TestTask.UsefulStaticMethods;
import com.HideoKuzeGits.TestTask.google.captcha.CaptchaPassService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by root on 01.11.14.
 */

@Controller
public class ProcessSiteController {


    @Autowired
    private ProcessSiteServiceAsyncWrap processSiteService;

    @Autowired
    private CaptchaPassService captchaService;


    @RequestMapping(value = "/processSite", method = RequestMethod.POST)
    public String processSite(@RequestParam(required = false) String url, ModelMap modelMap, RedirectAttributes redirectAttributes) {

        url = urlToCanonicalForm(url);

        if (Strings.isNullOrEmpty(url)) {
            redirectAttributes.addFlashAttribute("errorMassage", "EmptyUrl");
            return "redirect:/";
        }


        if (!UsefulStaticMethods.urlExists(url)) {
            redirectAttributes.addFlashAttribute("errorMassage", "Сайт не существует.");
            return "redirect:/";
        }

        String domain = null;

        try {
            domain = getDomain(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMassage", "Неправильная ссылка.");
            return "redirect:/";
        }

        try {
            processSiteService.processSite(domain);
        } catch (ToMachRequestsForSiteProcessingException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMassage", "Сайт слишком загруже попробуйте позже.");
            return "redirect:/";
        }


        return "redirect:/report?domain=" + domain;
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String getReport(@RequestParam(value = "domain") String domain, ModelMap modelMap) {

        if (captchaService.isCaptchaReached()){
            modelMap.addAttribute("redirectTo", "/report?domain=" + domain);
            return "redirect:/captcha/captchaPage";
        }

        SiteStatisticResponse siteStatisticResponse = processSiteService.getSiteStatisticResponse(domain);
        modelMap.put("report", siteStatisticResponse);

        return "siteStatistic";

    }

    private String getDomain(String url) throws MalformedURLException {

        url = urlToCanonicalForm(url);

        URL urlObject = new URL(url);
        return "http://" + urlObject.getHost();
    }

    private String urlToCanonicalForm(String url) {
        url = url.replaceAll("^((http://)?www\\.)", "");

        if (!url.startsWith("http"))
            url = "http://" + url;
        return url;
    }

}
