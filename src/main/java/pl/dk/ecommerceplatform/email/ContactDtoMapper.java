package pl.dk.ecommerceplatform.email;

import pl.dk.ecommerceplatform.email.dtos.ContactDto;

import java.time.LocalDateTime;

class ContactDtoMapper {

    public static Contact map(ContactDto contactDto) {
        return Contact.builder()
                .email(contactDto.sender())
                .subject(contactDto.subject())
                .message(contactDto.message())
                .dateOfPosting(LocalDateTime.now())
                .build();
    }

    public static ContactDto map(Contact contact) {
        return ContactDto.builder()
                .id(contact.getId())
                .sender(contact.getEmail())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .build();
    }

}
