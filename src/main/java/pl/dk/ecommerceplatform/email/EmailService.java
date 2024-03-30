package pl.dk.ecommerceplatform.email;

import pl.dk.ecommerceplatform.email.dtos.ContactDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

public interface EmailService {

    ContactDto sendContactMessage(ContactDto contactDto);
    void createAndSendRegistrationConfirmationEmail(UserDto userDto, String uri);
}
