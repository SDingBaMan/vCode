package com.sdingba.vcode.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdingba.vcode.config.VcodeConfig;
import com.sdingba.vcode.server.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

@RestController
@RequestMapping(value = "/v0.1/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    /**
     * 生成图片验证码
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public void genCaptcha(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getParameter("token");
        // TODO:有效性检验优化
        Assert.hasText(token);
        captchaService.genCaptcha(token, resp);
    }

    /**
     * 查询图片验证码
     */
    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    public Map<String, Object> findCaptcha(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getParameter("token");
        String reqCaptchaCode = req.getParameter("captchaCode");
        // TODO:有效性检验优化
        Assert.hasText(token);
        Assert.hasText(reqCaptchaCode);
        String captchaCode = captchaService.findCaptcha(token);

        Map<String, Object> params = Maps.newHashMap();
        boolean isValid = false;
        if (reqCaptchaCode.equals(captchaCode)) {
            isValid = true;
        }
        params.put("valid", isValid);
        return params;
    }

    @Resource
    private VcodeConfig vcodeConfig;

    @RequestMapping(value = "a")
    public void test() {
        System.out.println(vcodeConfig.getPassword() + " " + vcodeConfig.getHost());
        System.out.println(vcodeConfig.getCacheType());
        System.out.println(vcodeConfig.getPort());
    }
}