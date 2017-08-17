package com.sdingba.vcode.factory;

import com.sdingba.vcode.imageUtils.ImageBase;
import com.sdingba.vcode.imageUtils.ImageUtils;
import com.sdingba.vcode.imageUtils.ImageUtils2;

/**
 * Created by SDingBa.xiong on 17-8-17. 验证码图片工厂
 */
public class ImageFactory {

    public static ImageBase getImageUtil(int imageType) {
        ImageBase imageBase;
        switch (imageType) {
        case 1:
            imageBase = new ImageUtils();
            break;
        case 2:
            imageBase = new ImageUtils2();
            break;
        default:
            imageBase = new ImageUtils();
        }
        return imageBase;
    }

}
