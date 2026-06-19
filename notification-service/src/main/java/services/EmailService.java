package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}
    
	public void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to {} - subject: {}", to, subject);
        } catch (MailException e) {
            log.warn("Could not send email to {} (SMTP not configured?): {}", to, e.getMessage());
        }
    }
}
