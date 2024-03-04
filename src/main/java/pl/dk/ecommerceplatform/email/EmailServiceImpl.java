package pl.dk.ecommerceplatform.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.confirmationToken.TokenService;
import pl.dk.ecommerceplatform.email.dtos.CreateEmailDto;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

@Service
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TokenService tokenService;

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

    private void createAndSendConfirmationEmail(CreateEmailDto createEmailDto) {
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

    public void createAndSendRegistrationConfirmationEmail(UserDto userDto, String uri) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Registration confirmation");
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setTo(userDto.email());
        simpleMailMessage.setText(this.createRegistrationConfirmationMessage(userDto, uri));
        javaMailSender.send(simpleMailMessage);
    }

    private String createRegistrationConfirmationMessage(UserDto userDto, String uri) {
        return """
                Thanks %s for registering on our service.
                To activate your account, paste the link below into Postman or a similar app using the PATCH method:
                %s. The activation link is valid for 15 minutes.
                
                ECommercePlatform Team.
                """
                .trim()
                .formatted(userDto.firstName(), uri);
    }
}
