package pl.dk.ecommerceplatform.email;

import pl.dk.ecommerceplatform.email.dtos.CreateEmailDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

public interface EmailService {

    void sendContactMessage(CreateEmailDto createEmailDto);
    void createAndSendRegistrationConfirmationEmail(UserDto userDto, String uri);
}
