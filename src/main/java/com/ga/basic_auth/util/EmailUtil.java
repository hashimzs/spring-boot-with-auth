package com.ga.basic_auth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("library"); // Optional: specify the sender's email

        mailSender.send(message);
        System.out.println("Email sent successfully to " + to);
    }


    public static void main(String[] args) {
        String to = "hashimmohd1211@gmail.com";
        String subject = "Test Email";
        String body = "Hello, this is a test email!";

        EmailUtil emailUtil=new EmailUtil();

        emailUtil.sendEmail(to, subject, body);
        System.out.println("email sent");
    }
}
