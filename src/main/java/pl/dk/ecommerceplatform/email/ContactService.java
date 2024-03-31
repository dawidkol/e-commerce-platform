package pl.dk.ecommerceplatform.email;

import pl.dk.ecommerceplatform.email.dtos.ContactDto;
import pl.dk.ecommerceplatform.email.dtos.ContactResponseDto;

interface ContactService {

    ContactDto postContactMessage(ContactDto contactDto);
    void createResponseMessage(ContactResponseDto ContactResponseDto);
}
