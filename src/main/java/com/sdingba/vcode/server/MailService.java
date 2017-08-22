package com.sdingba.vcode.server;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sdingba.vcode.Contants.VcodeReslutEnum;
import com.sdingba.vcode.mail.SendMailService;

/**
 * Created by SDingBa.xiong on 17-8-22.
 */
@Service
public class MailService extends BaseService {
    @Resource
    private SendMailService sendMailService;

    /**
     * 获取 短信发送验证码
     *
     * @param mail mail
     * @param key key
     */
    public int genMailData(String mail, String key) {
        String generate = generateVerifyCode(6);
        try {
            if (StringUtils.isNotEmpty(generate)) {
                sendMailService.sendMail(mail, "vcode", generate);

                redisBaseServer.addValue(mail + key, generate);

            }
        } catch (Exception e) {
            LOGGER.error("send_sms_code_error {}", e);
        }
        return VcodeReslutEnum.FAIL.getCode();
    }

    public String findCaptcha(String mail, String key) {
        return redisBaseServer.getValue(mail + key);
    }

}
