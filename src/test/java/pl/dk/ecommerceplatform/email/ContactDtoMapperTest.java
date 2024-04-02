package pl.dk.ecommerceplatform.email;

import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.email.dtos.ContactDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ContactDtoMapperTest {

    @Test
    void itShouldMapToContactDto() {
        // Given
        Contact contact = Contact.builder()
                .id(1L)
                .email("john@doe.com")
                .subject("test subject")
                .message("This is example message")
                .dateOfPosting(LocalDateTime.now().minusDays(1))
                .build();

        // When
        ContactDto contactDto = ContactDtoMapper.map(contact);

        // Then
        assertAll(
                () -> assertThat(contactDto.id()).isEqualTo(contact.getId()),
                () -> assertThat(contactDto.sender()).isEqualTo(contact.getEmail()),
                () -> assertThat(contactDto.message()).isEqualTo(contact.getMessage())
        );
    }

    @Test
    void itShouldMapToContact() {
        // Given
        ContactDto contactDto = ContactDto.builder()
                .sender("john@doe.com")
                .subject("test subject")
                .message("This is example message")
                .build();

        // When
        Contact contact = ContactDtoMapper.map(contactDto);

        // Then
        assertAll(
                () -> assertThat(contact.getId()).isNull(),
                () -> assertThat(contact.getEmail()).isEqualTo(contactDto.sender()),
                () -> assertThat(contact.getMessage()).isEqualTo(contactDto.message()),
                () -> assertThat(contact.getDateOfPosting()).isBefore(LocalDateTime.now()),
                () -> assertThat(contact.getReplyDate()).isNull(),
                () -> assertThat(contact.getReplyMessage()).isNull()
        );
    }
}