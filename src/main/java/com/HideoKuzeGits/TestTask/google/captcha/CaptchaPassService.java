package com.HideoKuzeGits.TestTask.google.captcha;

import com.HideoKuzeGits.TestTask.google.search.GoogleSearchApi;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by root on 08.11.14.
 */

public class CaptchaPassService {

    private static Logger log = Logger.getLogger(CaptchaController.class.getName());


    private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36";

    private AtomicBoolean captchaReached = new AtomicBoolean(false);

    public static final String CAPTCHA_URL = "http://ipv4.google.com/sorry/CaptchaRedirect";
    public static final String IMAGE_URL = "http://ipv4.google.com/sorry/image?id=";
    public static final String IMAGE_URL_POSTFIX = "&hl=ru";

    private Map<String, String> cookies;
    private byte[] captchaImageBytes;
    private String id;

    @Autowired
    private GoogleSearchApi googleSearchApi;

    public synchronized boolean isCaptchaReached() {
        return captchaReached.get();
    }

    public synchronized void captchaReached() throws CaptchaNotPasseException {

        log.info("Captcha reached.");

        //Для того что бы получение капчи
            try {
                log.info("This is the first request for a captcha. Image will be loaded");
                getCaptcha();
            } catch (IOException e) {
                log.info("Captcha image load was falled.");
                e.printStackTrace();
                throw new CaptchaNotPasseException("Can't load captcha image.");
            }

        captchaReached.set(true);
    }

    public void getCaptcha() throws IOException {

        Document captchaPage = Jsoup
                .connect(CAPTCHA_URL)
                .ignoreHttpErrors(true)
                .userAgent(userAgent)
                .get();
        id = captchaPage.select("[name=id]").attr("value");

        String imageUrl = IMAGE_URL + id + IMAGE_URL_POSTFIX;


        Connection.Response captchaImageResponse = Jsoup.connect(imageUrl)
                .url(imageUrl)
                .ignoreContentType(true)
                .execute();
        cookies = captchaImageResponse.cookies();
        captchaImageBytes = captchaImageResponse.bodyAsBytes();

        log.info("Captcha image was successfully load.");

    }

    public void passCaptcha(String captchaValue) throws CaptchaNotPasseException {

        if (!captchaReached.get()){
            log.info("Try to pass captcha value without the need for captcha.");
            return;
        }

        captchaReached.set(false);

        Connection.Response redirectToGoogleCom = null;
        try {
            redirectToGoogleCom = Jsoup.connect(CAPTCHA_URL)
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .data("id", id)
                    .data("captcha", captchaValue)
                    .data("continue", "http://www.google.com/")
                    .cookies(cookies)
                    .execute();
        } catch (IOException e) {
            log.info("Pass captcha to google was falled.");
            captchaReached();
            throw new CaptchaNotPasseException(e);
        }

        if (!redirectToGoogleCom.url().toString().contains("http://www.google.com")) {
            log.info("Wrong captcha value.");
            captchaReached();
            throw new CaptchaNotPasseException("Don't redirects to http://www.google.com/");
        }



        googleSearchApi.captchaPassedCallBack(cookies);
    }


    public Map<String, String> getCookies() {
        return cookies;
    }

    public byte[] getCaptchaImageBytes() {
        return captchaImageBytes;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }


    //<editor-fold desc="JFrame to pass captcha">
    /*
    public static String getImageText(final Image image) throws IOException {

        final JTextField captchaValue = new JTextField(10);

        new Thread() {
            @Override
            public void run() {

                System.out.println("Load image into frame...");
                JLabel label = new JLabel(new ImageIcon(image));
                final JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Container contentPane = f.getContentPane();
                contentPane.add(BorderLayout.CENTER, label);
                contentPane.add(BorderLayout.SOUTH, captchaValue);
                JButton okButton = new JButton("Ok");
                okButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        latch.countDown();
                        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                    }
                });
                contentPane.add(BorderLayout.NORTH, okButton);

                f.pack();
                f.setLocation(200, 200);
                f.setVisible(true);
            }
        }.start();


        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return captchaValue.getText();
    }*/
    //</editor-fold>

}
