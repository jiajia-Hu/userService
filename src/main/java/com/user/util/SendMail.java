package com.user.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiajia on 2017/8/4
 */
@Component
public class SendMail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${ebao.address}")
    private String severHost;

    public void sendTemplateMail(String userEmail, String validCode) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("jiajiaTest@qq.com");
        helper.setTo(userEmail);
        helper.setSubject("主题：注册确认");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("username", userEmail);
        model.put("validCode", validCode);
        model.put("severHost",severHost);
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        // 设定去哪里读取相应的ftl模板
        cfg.setClassForTemplateLoading(this.getClass(), "/template");
        // 在模板文件目录中寻找名称为name的模板文件
        Template template = cfg.getTemplate("sendEmial.html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        helper.setText(html, true);
        javaMailSender.send(mimeMessage);
    }

}