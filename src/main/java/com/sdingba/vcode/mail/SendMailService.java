package com.sdingba.vcode.mail;

import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

import com.sdingba.vcode.config.VcodeConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
    private static final String MAIL_ACCOUNT = "xxxx";
    private static final String MAIL_PASS = "xxxx";
    private static final String SMTP_SERVER = "smtp.263.net";

    private SendMailService() {
    }

    @Resource
    private VcodeConfig vcodeConfig;

    /**
     * @param mailAddress 邮件接收人地址 多邮件,分割
     * @param subject 邮件主题
     * @param content 邮件内容
     **/
    public void sendMail(String mailAddress, String subject, String content) throws MessagingException {

        sendEmail(mailAddress, subject, content, null);
    }

    public void sendMailAttachment(String mailAddress, String subject, String content, String filePath)
            throws MessagingException {

        sendEmail(mailAddress, subject, content, filePath);
    }

    private void sendEmail(String mailAddress, String subject, String content, String filePath)
            throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.host", vcodeConfig.getSmtpServer());
        props.setProperty("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setSubject(subject);
        msg.setText(content);
        msg.setFrom(new InternetAddress(vcodeConfig.getMailAccount()));
        msg.setRecipients(Message.RecipientType.TO, getAddress(mailAddress));
        if (!StringUtils.isEmpty(filePath)) {
            // 创建邮件的各个 MimeBodyPart 部分
            MimeBodyPart bodyPart = createAttachment(filePath);
            MimeMultipart multipart = new MimeMultipart("mixed");
            multipart.addBodyPart(bodyPart);
            msg.setContent(multipart);
            msg.saveChanges();
        }
        Transport transport = session.getTransport();
        transport.connect(vcodeConfig.getMailAccount(), vcodeConfig.getMailPass());
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }

    /**
     * 获取邮件地址
     */
    private InternetAddress[] getAddress(String mailAddress) throws AddressException {
        ArrayList<InternetAddress> list = new ArrayList<>();
        String[] mailSend = mailAddress.split(",");
        for (String mailTo : mailSend) {
            list.add(new InternetAddress(mailTo));
        }
        return list.toArray(new InternetAddress[list.size()]);
    }

    /**
     * 根据传入的文件路径创建附件并返回
     */
    private MimeBodyPart createAttachment(String fileName) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(fileName);
        attachmentPart.setDataHandler(new DataHandler(fds));
        attachmentPart.setFileName(fds.getName());
        return attachmentPart;
    }
}
