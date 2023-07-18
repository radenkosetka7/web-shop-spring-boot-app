package com.example.webshop.services.impl;

import com.example.webshop.services.EmailService;
import com.example.webshop.services.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final LoggerService loggerService;
    @Value("${spring.mail.username}") private String sender;


    @Override
    public void sendEmail(String recipient, String code)
    {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(recipient);
        mailMessage.setSubject("WebShopIP - Verification");
        String text = "Welcome to WebShopIP application \n\nACTIVATION CODE: " + code;
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
        loggerService.saveLog(" The mail has sent to: " + recipient,this.getClass().getName());
        //logger

    }
}
