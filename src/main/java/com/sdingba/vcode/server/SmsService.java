package com.sdingba.vcode.server;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sdingba.vcode.Contants.VcodeReslutEnum;
import com.sdingba.vcode.sms.SmsMessage;

/**
 * Created by SDingBa.xiong on 17-8-22.
 */
@Service
public class SmsService extends BaseService {

    @Resource
    private SmsMessage smsMessage;

    /**
     * 获取 短信发送验证码
     * 
     * @param phone phone
     * @param key key
     */
    public int genSmsData(String phone, String key) {
        String generate = generateVerifyCode(6);
        try {
            if (StringUtils.isNotEmpty(generate)) {
                boolean isSmsSuccess = smsMessage.sendSms(phone, generate, "NO-1");
                if (isSmsSuccess) {
                    redisBaseServer.addValue(phone + key, generate);
                    return VcodeReslutEnum.SUCESS.getCode();
                }
            }
        } catch (Exception e) {
            LOGGER.error("send_sms_code_error {}", e);
        }
        return VcodeReslutEnum.FAIL.getCode();
    }

    /**
     * 查询图片验证码
     */
    public String findSmsCaptcha(String phone, String key) {
        return redisBaseServer.getValue(phone + key);
    }

}
