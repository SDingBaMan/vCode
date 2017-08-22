package com.sdingba.vcode.server;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by SDingBa.xiong on 17-8-22.
 */
public abstract class BaseService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    private static final String VERIFY_CODES = "1234567890";

    @Resource
    protected RedisBaseServer redisBaseServer;

    protected String generateVerifyCode(int verifySize) {
        return generateVerifyCode(verifySize, VERIFY_CODES);
    }

    protected String generateVerifyCode(int verifySize, String sources) {
        if (StringUtils.isEmpty(sources)) {
            sources = VERIFY_CODES;
        }
        Random rand = new Random(System.currentTimeMillis());
        int codesLen = sources.length();
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }
}
