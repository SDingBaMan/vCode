package com.sdingba.vcode.server;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.sdingba.vcode.Contants.VcodeReslutEnum;
import com.sdingba.vcode.config.VcodeConfig;
import com.sdingba.vcode.factory.ImageFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    @Resource
    private VcodeConfig vcodeConfig;

    @Resource
    private RedisBaseServer redisBaseServer;

    /**
     * 生成图片验证码
     */
    public int genCaptcha(String key, HttpServletResponse resp) {
        int imageType = StringUtils.isNumeric(vcodeConfig.getImage()) ? Integer.parseInt(vcodeConfig.getImage()) : 0;
        String verifyCode = ImageFactory.getImageUtil(imageType).genCaptcha(key, resp);
        if (StringUtils.isNotEmpty(verifyCode)) {
            redisBaseServer.addValue(key, verifyCode);
            return VcodeReslutEnum.SUCESS.getCode();
        } else {
            return VcodeReslutEnum.FAIL.getCode();
        }
    }

    /**
     * 查询图片验证码
     *
     */
    public String findCaptcha(String key) {
        return redisBaseServer.getValue(key);
    }

}