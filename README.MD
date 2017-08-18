
验证码项目，使用验证码快速开发login功能，
本项目提供mvn版本，开箱即用，

本项目暂时使用redis存储验证码数据；


前提条件。
    配置配置文件，含redis
    mvn构建，支持gradle


quick start:
   1 mvn :
        导入依赖文件：
        <dependency>
        		<groupId>com.sdingba.vcode</groupId>
        		<artifactId>vcode-plug</artifactId>
        		<version>1.0-SNAPSHOT</version>
        </dependency>

   2 // 获取验证码图片，需要传入HttpServletResponse
    captchaService.genCaptcha(key, resp);

   3 // 获取验证码数据
    captchaService.findCaptcha(key);

   4 配置文件：
    vcode.properties
    vcode.redis.host=127.0.0.1
    vcode.redis.port=6379
    vcode.redis.password=root
    vcode.redis.database=2

    vcode.image=2    // 选择 图片样式类型
    // vcode.cacheType=redis     // 选择验证码存储方式，目前redis