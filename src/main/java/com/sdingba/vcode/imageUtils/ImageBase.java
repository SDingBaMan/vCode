package com.sdingba.vcode.imageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by SDingBa.xiong on 17-8-17.
 */
public abstract class ImageBase {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ImageBase.class);

    // 图片的宽度
    protected static final int CAPTCHA_WIDTH = 100;
    // 图片的高度
    protected static final int CAPTCHA_HEIGHT = 40;
    // 验证码的个数
    protected static final int CAPTCHA_CODECOUNT = 4;

    /**
     * 获取验证码流
     */
    public abstract String genCaptcha(String key, HttpServletResponse resp);
}
