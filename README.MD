
验证码项目，使用验证码快速开发login验证码功能，
本项目提供mvn版本，开箱即用，可以为mvn/gradle提供依赖

本项目暂时只使用redis存储验证码数据；


前提条件。
    配置配置文件，含redis
    mvn构建，支持gradle


quick start:
   1 mvn :
        mvn install  -- 打包前 先 设置打包资源pom.xml文件设置
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
    // vcode.cacheType=redis     // 选择验证码存储方式，目前redis 不需要填写


2017-08-22：
 add sms 短信发送模块，依赖redis,
    配置：
         vcode.smsAppKey=isSmsSuccess
         vcode.smsAppSecret=isSmsSuccess
         vcode.smsSign=isSmsSuccess
    使用SmsService进行操作


 add mail 邮箱发送验证码
    配置：
         vcode.mailAccount=xxxx
         vcode.mailPass=xxxx
         vcode.smtpServer=smtp.263.net
    使用MailService进行操作


2017-08-23
 添加http请求；
 如果 验证码单独运行一个项目，其他子项目使用http请求获取，需要添加authKey简单验证；
 vcode.auth.authKey=sdfsafdasdfasdfkjaskdljfklasdjlfkjsdkljflk
 Controller类，详见；