package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import springboot.model.Email;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("aepc.lacina@gmail.com");
        message.setSubject(email.getSubject());
        message.setText("From: ".concat(email.getUsername()).concat("\n").concat(email.getMessage()));
        message.setReplyTo(email.getEmail());
        message.setFrom(email.getEmail());

        emailSender.send(message);
    }
}
