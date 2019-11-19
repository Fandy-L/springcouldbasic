package com.to8to.tbt.msc.utils;

import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.dto.MailSenderDTO;
import com.to8to.tbt.msc.entity.MailAuthenticator;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * @author juntao.guo
 */
@Slf4j
public class SendMailUtils {

    private static final String DEFAULT_PERSONAL = "to8to-MSGC";

    /**
     * 发送邮件
     *
     * @param toUserAddress
     * @param title
     * @param content
     * @param personal
     * @return
     */
    public static boolean sendMail(String toUserAddress, String title, String content, String personal) {
        List<String> maillist = Arrays.asList(MsgCenterConfiguration.MAIL_USERNAME.split("\\|"));
        String fromUsername;
        if (maillist.size() == 1) {
            fromUsername = maillist.get(0);
        } else {
            int i = new Random().nextInt(maillist.size());
            fromUsername = maillist.get(i);
        }
        MailSenderDTO mailInfo = new MailSenderDTO();
        mailInfo.setMailServerHost(MsgCenterConfiguration.MAIL_SERVER);
        mailInfo.setMailServerPort(MsgCenterConfiguration.MAIL_PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(fromUsername);
        mailInfo.setPassword(MsgCenterConfiguration.MAIL_PASSWORD);
        mailInfo.setFromAddress(fromUsername);
        mailInfo.setToAddress(toUserAddress);
        mailInfo.setSubject(title);
        mailInfo.setContent(content);
        mailInfo.setPersonal(null == personal ? DEFAULT_PERSONAL : personal);
        return sendHtmlMail(mailInfo);
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailSenderDTO mailInfo) {
        long start = System.currentTimeMillis();
        // 判断是否需要身份认证
        MailAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getPersonal(), "UTF-8");
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            log.info(LogUtils.buildTemplate("costTime title"), (System.currentTimeMillis() - start), mailMessage.getSubject());
            return true;
        } catch (Exception ex) {
            log.warn(LogUtils.buildTemplate("toAddress subject"), mailInfo.getToAddress(), mailInfo.getSubject(), ex);
        }
        return false;
    }
}
