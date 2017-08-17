package com.sdingba.vcode.imageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by SDingBa.xiong on 17-8-17.
 */
public abstract class ImageContants {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils2.class);

    // 图片的宽度
    protected static final int CAPTCHA_WIDTH = 90;
    // 图片的高度
    protected static final int CAPTCHA_HEIGHT = 20;
    // 验证码的个数
    protected static final int CAPTCHA_CODECOUNT = 4;

    // 过期时间为60秒
    protected static final long EXPIRE_MINUTES = 60;

    /**
     * 获取验证码流
     */
    public abstract void genCaptcha(String key, HttpServletResponse resp);
}
