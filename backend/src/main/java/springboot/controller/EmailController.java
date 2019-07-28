package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.model.Email;
 
@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class EmailController {
 
    @Autowired
    public JavaMailSender emailSender;
 
	@ApiOperation("Permite registrar um email de um user para o admin.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Email.class) })
	@RequestMapping(value = "/email", method = RequestMethod.POST)
    public void sendSimpleEmail(@RequestBody Email email) {
 
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
         
        message.setTo("aepc.lacina@gmail.com");
        message.setSubject(email.getSubject());
        message.setText("From: " + email.getUsername() + "\n" + email.getMessage());
        message.setReplyTo(email.getEmail()	);
        message.setFrom(email.getEmail());

        // Send Message!
        this.emailSender.send(message);
    }
}