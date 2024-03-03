package pl.dk.ecommerceplatform.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.email.dtos.CreateEmailDto;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

@Service
@RequiredArgsConstructor
class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${app.mail.username}")
    private String email;
    private final Logger logger = UtilsService.getLogger(this.getClass());

    public void sendContactMessage(CreateEmailDto createEmailDto) {
        try {
            this.createAndSendContactEmail(createEmailDto);
            this.createAndSendConfirmationEmail(createEmailDto);
        } catch (MailException ex) {
            throw new ServerException();
        }
    }

    private void createAndSendContactEmail(CreateEmailDto createEmailDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setReplyTo(createEmailDto.sender());
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(createEmailDto.subject());
        simpleMailMessage.setText(createEmailDto.message());
        logger.debug("Starting sending email");
        javaMailSender.send(simpleMailMessage);
        logger.debug("Email sent");
    }

    public void createAndSendConfirmationEmail(CreateEmailDto createEmailDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String sender = createEmailDto.sender();
        simpleMailMessage.setSubject("Auto response - \"%s\"".formatted(createEmailDto.subject()));
        simpleMailMessage.setTo(sender);
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setText(this.createAutoResponseMessage(sender));
        logger.debug("Starting sending response");
        javaMailSender.send(simpleMailMessage);
        logger.debug("Response email sent");
    }

    private String createAutoResponseMessage(String email) {
        return """
                Thanks %s for contacting us, we will contact you soon.
                                
                ECommercePlatform Team
                """.formatted(email);
    }

}
