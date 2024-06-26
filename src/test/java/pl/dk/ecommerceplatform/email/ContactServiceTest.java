package pl.dk.ecommerceplatform.email;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.dk.ecommerceplatform.email.dtos.ContactDto;
import pl.dk.ecommerceplatform.email.dtos.ContactResponseDto;
import pl.dk.ecommerceplatform.error.exceptions.email.ContactNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    private ContactService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private ContactRepository contactRepository;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ContactServiceImpl(javaMailSender, contactRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSaveContact() {
        // Given
        ContactDto contactDto = ContactDto
                .builder()
                .sender("sender@example.com")
                .subject("Test Subject")
                .message("Test Message")
                .build();

        Contact contact = Contact.builder()
                .id(1L)
                .email(contactDto.sender())
                .subject(contactDto.subject())
                .message(contactDto.message())
                .dateOfPosting(LocalDateTime.now())
                .build();

        // When
        doNothing().when(javaMailSender).send((SimpleMailMessage) any());
        when(contactRepository.save(any())).thenReturn(contact);

        // Then
        ContactDto dto = underTest.postContactMessage(contactDto);

        assertAll(
                () -> assertThat(dto.id()).isNotNull(),
                () -> assertThat(dto.sender()).isEqualTo(contact.getEmail()),
                () -> assertThat(dto.subject()).isEqualTo(contact.getSubject()),
                () -> assertThat(dto.message()).isEqualTo(contact.getMessage())
        );
    }

    @Test
    void itShouldCreateResponseMessage() {
        // Given
        ContactResponseDto contactResponseDto = new ContactResponseDto(1L, "This is example response message");
        Contact contact = Contact.builder()
                .id(1L)
                .email("john@doe.com")
                .subject("test subject")
                .message("This is example message")
                .dateOfPosting(LocalDateTime.now().minusDays(1))
                .build();

        when(contactRepository.findById(contactResponseDto.contactId())).thenReturn(Optional.of(contact));

        // When
        underTest.createResponseMessage(contactResponseDto);

        // Then
        verify(contactRepository, times(1)).findById(contactResponseDto.contactId());

    }

    @Test
    void itShouldThrowContactNotFoundExceptionWhenUserProvideInvalidID() {
        // Given
        ContactResponseDto contactResponseDto = new ContactResponseDto(1L, "This is example response message");
        Contact contact = Contact.builder()
                .id(1L)
                .email("john@doe.com")
                .subject("test subject")
                .message("This is example message")
                .dateOfPosting(LocalDateTime.now().minusDays(1))
                .build();

        when(contactRepository.findById(contactResponseDto.contactId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ContactNotFoundException.class, () -> underTest.createResponseMessage(contactResponseDto));
        verify(contactRepository, times(1)).findById(contactResponseDto.contactId());

    }
}