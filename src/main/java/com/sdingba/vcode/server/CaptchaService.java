package com.sdingba.vcode.server;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.sdingba.vcode.imageUtils.ImageUtils2;
import org.springframework.stereotype.Service;

import com.sdingba.vcode.imageUtils.ImageUtils;

@Service
public class CaptchaService {

    @Resource
    private ImageUtils imageUtils;
    @Resource
    private ImageUtils2 imageUtils2;

    @Resource
    private RedisBaseServer redisBaseDao;

    /**
     * 生成图片验证码
     */
    public void genCaptcha(String key, HttpServletResponse resp) {
        imageUtils2.genCaptcha(key, resp);
    }

    /**
     * 查询图片验证码
     *
     */
    public String findCaptcha(String key) {
        return redisBaseDao.getValue(key);
    }

}