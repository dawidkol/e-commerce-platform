package pl.dk.ecommerceplatform.email;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.confirmationToken.TokenService;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.email.dtos.ContactDto;
import pl.dk.ecommerceplatform.email.dtos.ContactResponseDto;
import pl.dk.ecommerceplatform.error.exceptions.email.ContactNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.user.dtos.ActivationLinkDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.net.URI;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
class EmailAspect {

    private final JavaMailSender javaMailSender;
    private final TokenService tokenService;
    private final ContactRepository contactRepository;
    private final Logger logger = UtilsService.getLogger(this.getClass());

    @Value("${app.mail.username}")
    private String email;

    @AfterReturning(pointcut = "execution(* pl.dk.ecommerceplatform.email.ContactController.postContactMessage(..))", returning = "responseEntity")
    @Async
    public void sendAutoResponseEmailAfterContact(ResponseEntity<ContactDto> responseEntity) {
        ContactDto contactDto = responseEntity.getBody();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String sender = contactDto.sender();
        simpleMailMessage.setSubject("Auto response - \"%s\"".formatted(contactDto.subject()));
        simpleMailMessage.setTo(sender);
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setText(this.createAutoResponseMessage(sender));
        logger.debug("Starting sending response message to {}", sender);
        javaMailSender.send(simpleMailMessage);
        logger.debug("Response email sent to {}", sender);
    }

    private String createAutoResponseMessage(String email) {
        return """
                Thanks %s for contacting us, we will contact you soon.
                                
                ECommercePlatform Team
                """.formatted(email);
    }

    @AfterReturning(pointcut = "execution(* pl.dk.ecommerceplatform.user.UserController.register(..))",
            returning = "responseEntity")
    @Async
    public void createAndSendRegistrationConfirmationEmail(ResponseEntity<UserDto> responseEntity) {
        URI uri = responseEntity.getHeaders().getLocation();
        UserDto userDto = responseEntity.getBody();

        TokenDto tokenDto = tokenService.generateConfirmationToken(userDto.email());
        logger.debug("Generated token: {}", tokenDto.token());
        String accountActivationLink = ServletUriComponentsBuilder.fromUri(uri)
                .path("/{token}")
                .buildAndExpand(tokenDto.token())
                .toUriString();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Registration confirmation");
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setTo(userDto.email());
        simpleMailMessage.setText(this.createRegistrationConfirmationMessage(userDto, accountActivationLink));
        try {
            logger.debug("Starting sending account activation email to {}", userDto.email());
            javaMailSender.send(simpleMailMessage);
            logger.debug("Account activation email sent to {}", userDto.email());
        } catch (MailException ex) {
            throw new ServerException();
        }
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

    @AfterReturning(pointcut = "execution(* pl.dk.ecommerceplatform.user.UserController.regenerateAccountConfirmationToken(..))",
            returning = "responseEntity")
    @Async
    public void regenerateAccountConfirmationToken(ResponseEntity<ActivationLinkDto> responseEntity) {

        String activationLinkUri = responseEntity.getHeaders().getLocation().toString();
        UserDto userDto = responseEntity.getBody().userDto();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("ECommercePlatform: confirm account");
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setTo(userDto.email());
        simpleMailMessage.setText(this.createAccountConfirmationMessage(userDto, activationLinkUri));
        try {
            logger.debug("Starting sending account activation email to {}", userDto.email());
            javaMailSender.send(simpleMailMessage);
            logger.debug("Account activation email sent to {}", userDto.email());
        } catch (MailException ex) {
            throw new ServerException();
        }
    }

    private String createAccountConfirmationMessage(UserDto userDto, String uri) {
        return """
                Hey %s,
                To activate your account, paste the link below into Postman or a similar app using the PATCH method:
                %s. The activation link is valid for 15 minutes.
                                
                ECommercePlatform Team.
                """
                .trim()
                .formatted(userDto.firstName(), uri);
    }

    @AfterReturning(pointcut = "execution(* pl.dk.ecommerceplatform.email.ContactController.sendResponse(..))",
            returning = "responseEntity")
    @Async
    public void sendResponseMessage(ResponseEntity<ContactResponseDto> responseEntity) {
        ContactResponseDto contactResponseDto = responseEntity.getBody();
        Long contactId = contactResponseDto.contactId();
        String response = contactResponseDto.response();
        Contact contact = contactRepository.findById(contactId).
                orElseThrow(() -> new ContactNotFoundException("Contact with id %d not found".formatted(contactId)));
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject("Response:".concat(contact.getSubject()));
        simpleMailMessage.setTo(contact.getEmail());
        simpleMailMessage.setText(response);
        logger.debug("Starting sending response email to {}", contact.getEmail());
        javaMailSender.send(simpleMailMessage);
        logger.debug("Response email sent to {}", contact.getEmail());
        contact.setReplyDate(LocalDateTime.now());
    }
}
