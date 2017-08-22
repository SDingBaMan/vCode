package com.sdingba.vcode.sms;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sdingba.vcode.config.VcodeConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sdingba.vcode.utils.HttpRequestUtil;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

/**
 */
@Service
public class SmsMessage {
    private static final String APP_KEY = "APP_KEY";
    private static final String APP_SECRET = "APP_SECRET";
    private static final String HTTP_SMS_URL = "http://dysmsapi.aliyuncs.com/?Signature=";
    private static final String SMS_SIGN = "sdingba.xiong";
    private static final String CODE_OK = "OK";
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsMessage.class);

    @Resource
    private VcodeConfig vcodeConfig;

    /**
     * @param phoneNumber 短信接收号码,多个手机号 , 分隔
     * @param paramString 短信参数json结构
     * @param smsTemplateCode 短信模板 .如果为空 则采用默认短信模板
     **/
    public boolean sendSms(String phoneNumber, String paramString, String smsTemplateCode) throws Exception {

        String code = "";
        String smsUrl = getSmsUrl(phoneNumber, paramString, smsTemplateCode);
        CloseableHttpResponse httpResponse = (CloseableHttpResponse) HttpRequestUtil.getResponse(smsUrl,
                Maps.newHashMap());
        if (httpResponse != null) {
            String responseText = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject = JSON.parseObject(responseText);
            code = jsonObject.getString("Code");
            if (CODE_OK.equals(code)) {
                LOGGER.info("sms_info={},phone={},sms_code={}", paramString, phoneNumber, code);
                return true;
            }
        }
        LOGGER.info("sms_info={},phone={},sms_code={}", paramString, phoneNumber, code);
        return false;
    }

    private String getSmsUrl(String phoneNumber, String paramString, String smsTemplateCode) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // 设置GMT时区
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        Map<String, String> paras = new HashMap<>();
        // 系统参数
        paras.put("AccessKeyId", vcodeConfig.getSmsAppKey());
        paras.put("SignatureNonce", UUID.randomUUID().toString());
        paras.put("Timestamp", df.format(new Date()));
        paras.put("SignatureMethod", "HMAC-SHA1");
        paras.put("SignatureVersion", "1.0");
        paras.put("Format", "JSON");
        // 业务API参数
        paras.put("Action", "SendSms");
        paras.put("Version", "2017-05-25");
        paras.put("RegionId", "cn-hangzhou");
        paras.put("SignName", vcodeConfig.getSmsSign());
        paras.put("PhoneNumbers", phoneNumber);
        paras.put("TemplateParam", paramString);
        paras.put("TemplateCode", smsTemplateCode);
        // 参数KEY排序
        TreeMap<String, String> sortParas = new TreeMap<>();
        sortParas.putAll(paras);
        // 构造待签名的字符串
        Iterator<String> it = sortParas.keySet().iterator();
        StringBuilder sortQueryStringTmp = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=")
                    .append(specialUrlEncode(paras.get(key)));
        }
        String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append("GET").append("&");
        stringToSign.append(specialUrlEncode("/")).append("&");
        stringToSign.append(specialUrlEncode(sortedQueryString));
        String sign = sign(vcodeConfig.getSmsAppSecret() + "&", stringToSign.toString());
        // 签名最后也要做特殊URL编码
        String signature = specialUrlEncode(sign);

        return HTTP_SMS_URL + signature + sortQueryStringTmp;
    }

    private static String specialUrlEncode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    private static String sign(String accessSecret, String stringToSign) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return new BASE64Encoder().encode(signData);
    }

}
