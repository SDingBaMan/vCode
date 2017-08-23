package com.sdingba.vcode.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.sdingba.vcode.Contants.VcodeReslutEnum;
import com.sdingba.vcode.config.VcodeConfig;
import com.sdingba.vcode.server.CaptchaService;
import com.sdingba.vcode.server.MailService;
import com.sdingba.vcode.server.SmsService;

/**
 * 测试 Controller 如果，验证码单独 项目，需要平配置authKey，并且使用http请求 生成/获取 验证码
 */
@RestController
@RequestMapping(value = "/v0.1/vCodePlug")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;
    @Resource
    private SmsService smsService;
    @Resource
    private MailService mailService;
    @Resource
    private VcodeConfig vcodeConfig;

    private static final String RESULT = "result";

    /**
     * 生成图片验证码
     */
    @RequestMapping(value = "vCode", method = RequestMethod.GET)
    public Map<String, Object> genCaptcha(HttpServletRequest req, HttpServletResponse resp, @RequestParam String key,
            @RequestParam String authKey) {
        Map<String, Object> result = Maps.newHashMap();
        if (StringUtils.isNotEmpty(key) && StringUtils.equals(vcodeConfig.getAuthKey(), authKey)) {
            captchaService.genCaptcha(key, resp);
            result.put(RESULT, VcodeReslutEnum.SUCESS.getCode());
        } else {
            result.put(RESULT, VcodeReslutEnum.FAIL.getCode());
        }
        return result;
    }

    /**
     * 查询图片验证码
     */
    @RequestMapping(value = "vcodeFind", method = RequestMethod.GET)
    public Map<String, Object> findCaptcha(HttpServletRequest req, @RequestParam String key,
            @RequestParam String authKey) {
        String reqCaptchaCode = req.getParameter("captchaCode");
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(reqCaptchaCode)
                && StringUtils.equals(vcodeConfig.getAuthKey(), authKey)) {
            String captchaCode = captchaService.findCaptcha(key);
            params.put("code", captchaCode);
            params.put(RESULT, VcodeReslutEnum.SUCESS.getCode());
        } else {
            params.put(RESULT, VcodeReslutEnum.FAIL.getCode());
        }
        return params;
    }

    @RequestMapping(value = "sms", method = RequestMethod.GET)
    public Map<String, Object> smsCaptcha(@RequestParam String phone, @RequestParam String key,
            @RequestParam String authKey) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(key)
                && StringUtils.equals(authKey, vcodeConfig.getAuthKey())) {
            map.put(RESULT, smsService.genSmsData(phone, key));
        } else {
            map.put(RESULT, VcodeReslutEnum.FAIL.getCode());
        }
        return map;
    }

    @RequestMapping(value = "smsFind", method = RequestMethod.GET)
    public Map<String, Object> smsFindCaptcha(@RequestParam String phone, @RequestParam String key,
            @RequestParam String authKey) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(key)
                && StringUtils.equals(authKey, vcodeConfig.getAuthKey())) {
            map.put(RESULT, VcodeReslutEnum.SUCESS.getCode());
            map.put("code", smsService.findSmsCaptcha(phone, key));
        } else {
            map.put(RESULT, VcodeReslutEnum.FAIL.getCode());
        }
        return map;
    }

    @RequestMapping(value = "mail", method = RequestMethod.GET)
    public Map<String, Object> mailCaptcha(@RequestParam String mail, @RequestParam String key,
            @RequestParam String authKey) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(mail) && StringUtils.isNotEmpty(key)
                && StringUtils.equals(authKey, vcodeConfig.getAuthKey())) {
            map.put(RESULT, mailService.genMailData(mail, key));
        } else {
            map.put(RESULT, VcodeReslutEnum.FAIL.getCode());
        }
        return map;
    }

    @RequestMapping(value = "mailFind", method = RequestMethod.GET)
    public Map<String, Object> mailFindCaptcha(@RequestParam String mail, @RequestParam String key,
            @RequestParam String authKey) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(mail) && StringUtils.isNotEmpty(key)
                && StringUtils.equals(authKey, vcodeConfig.getAuthKey())) {
            map.put(RESULT, VcodeReslutEnum.SUCESS.getCode());
            map.put("code", mailService.findCaptcha(mail, key));
        } else {
            map.put(RESULT, VcodeReslutEnum.FAIL.getCode());
        }
        return map;
    }

    @RequestMapping(value = "test")
    public void test() {

    }
}