package com.sdingba.vcode.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.sdingba.vcode.server.CaptchaService;

@RestController
@RequestMapping(value = "/v0.1/vCodePlug")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    /**
     * 生成图片验证码
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> genCaptcha(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> result = Maps.newHashMap();
        String key = req.getParameter("token");
        if (StringUtils.isNotEmpty(key)) {
            captchaService.genCaptcha(key, resp);
            result.put("result", true);
        } else {
            result.put("result", false);
        }
        return result;
    }

    /**
     * 查询图片验证码
     */
    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    public Map<String, Object> findCaptcha(HttpServletRequest req, HttpServletResponse resp) {
        String key = req.getParameter("token");
        String reqCaptchaCode = req.getParameter("captchaCode");
        boolean isValid = false;
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(reqCaptchaCode)) {
            String captchaCode = captchaService.findCaptcha(key);
            if (reqCaptchaCode.equals(captchaCode)) {
                isValid = true;
            }
        }
        params.put("valid", isValid);
        return params;
    }

    @RequestMapping(value = "test")
    public void test() {
    }
}