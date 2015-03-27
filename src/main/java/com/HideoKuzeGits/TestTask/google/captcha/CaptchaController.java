package com.HideoKuzeGits.TestTask.google.captcha;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by root on 08.11.14.
 */
@Controller
public class CaptchaController {

    private static Logger log = Logger.getLogger(CaptchaController.class.getName());

    @Autowired
    private CaptchaPassService captchaService;

    @RequestMapping(value = "/captcha/captchaPage", method = RequestMethod.GET)
    public String getCaptchaPage(@RequestParam(value = "redirectTo", required = false) String redirectTo,
                                 ModelMap modelMap){


        if (!captchaService.isCaptchaReached()){
            log.info("There is no captcha.");
            return "redirect:/";
        }

        if (Strings.isNullOrEmpty(redirectTo)){
            modelMap.put("redirectTo", "/captchaPage");
            modelMap.put("message", "Спасибо что решили помочь нам с вводм каптчь. <br>" +
                    " К сожалению вы не сказали нам откуда вы пришли потому обратно после ввода капчи мы вас отправить не соможем. <br>" +
                    "Как вам надоест вводить капчи можете перейти на нашу главную страницу <br>" +
                    "<a href=\"/\">Главная страница</a>");}

        modelMap.put("redirectTo", redirectTo);

        return "CaptchaPage";
    }

    @RequestMapping(value = "/captcha/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getCaptchaImage(){

        if (!captchaService.isCaptchaReached())
            return null;

        return captchaService.getCaptchaImageBytes();
    }


    @RequestMapping(value = "/captcha/passCaptcha", method = RequestMethod.POST)
    public String passCaptcha(@RequestParam(value = "redirectTo", required = true) String redirectTo,
                              @RequestParam(value = "captchaValue", required = true) String captchaValue,
                              ModelMap modelMap){



        try {
            captchaService.passCaptcha(captchaValue);
        } catch (CaptchaNotPasseException e) {
            e.printStackTrace();
            modelMap.addAttribute("redirectTo", redirectTo);
            return "redirect:/captcha/captchaPage";
        }

        return "redirect:" + redirectTo;
    }
}
