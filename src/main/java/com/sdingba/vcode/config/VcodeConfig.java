package com.sdingba.vcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by SDingBa.xiong on 17-8-17.
 */
@Component
@PropertySource("classpath:vcode.properties")
@ConfigurationProperties(prefix = "vcode")
public class VcodeConfig {

    @Value("${vcode.redis.host}")
    private String host;

    @Value("${vcode.redis.port}")
    private String port;

    @Value("${vcode.redis.password}")
    private String password;

    @Value("${vcode.redis.database}")
    private String database;

    private String cacheType;

    private String image;

    private String smsAppKey;

    private String smsAppSecret;

    private String smsSign;

    private String mailAccount;

    private String mailPass;

    private String smtpServer;

    @Value("${vcode.auth.authKey}")
    private String authKey;

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getMailAccount() {
        return mailAccount;
    }

    public void setMailAccount(String mailAccount) {
        this.mailAccount = mailAccount;
    }

    public String getMailPass() {
        return mailPass;
    }

    public void setMailPass(String mailPass) {
        this.mailPass = mailPass;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmsAppKey() {
        return smsAppKey;
    }

    public void setSmsAppKey(String smsAppKey) {
        this.smsAppKey = smsAppKey;
    }

    public String getSmsAppSecret() {
        return smsAppSecret;
    }

    public void setSmsAppSecret(String smsAppSecret) {
        this.smsAppSecret = smsAppSecret;
    }

    public String getSmsSign() {
        return smsSign;
    }

    public void setSmsSign(String smsSign) {
        this.smsSign = smsSign;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCacheType() {
        return cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
