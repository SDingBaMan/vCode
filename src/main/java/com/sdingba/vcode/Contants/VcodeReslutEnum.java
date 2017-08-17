package com.sdingba.vcode.Contants;

/**
 * Created by SDingBa.xiong on 17-8-17.
 */
public enum VcodeReslutEnum {

    SUCESS(10010, "验证码成功"),

    FAIL(10011, "验证码失败");

    private int code;
    private String describe;

    VcodeReslutEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
