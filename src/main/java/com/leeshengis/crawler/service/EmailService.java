package com.leeshengis.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * email service
 *
 * @author lisheng
 */
@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    /**
     * send html email
     *
     * @param to
     * @param title
     * @param htmlContent
     */
    public void sendHtmlEmail(String[] to, String title, String htmlContent) {
        try {
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sendFrom);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(htmlContent, true);
            mailSender.send(mimeMailMessage);
            log.info("email send success.");
        } catch (Exception e) {
            log.error("email send failed", e);
        }
    }
}
