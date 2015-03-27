package com.HideoKuzeGits.TestTask.google.captcha;

/**
 * Created by root on 08.11.14.
 */
public class CaptchaNotPasseException extends Exception {

    public CaptchaNotPasseException() {
        super();
    }

    public CaptchaNotPasseException(String message) {
        super(message);
    }

    public CaptchaNotPasseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaNotPasseException(Throwable cause) {
        super(cause);
    }
}
