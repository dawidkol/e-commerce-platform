package pl.dk.ecommerceplatform.email;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.dk.ecommerceplatform.email.dtos.ContactDto;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    private EmailService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private ContactRepository contactRepository;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EmailServiceImpl(javaMailSender, contactRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSaveContactToDatabaseAndSendConfirmationEmail() {
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
        ContactDto dto = underTest.sendContactMessage(contactDto);
        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender, times(1)).send(simpleMailMessageArgumentCaptor.capture());
        assertAll(
                () -> assertThat(dto.id()).isNotNull(),
                () -> assertThat(dto.sender()).isEqualTo(contact.getEmail()),
                () -> assertThat(dto.subject()).isEqualTo(contact.getSubject()),
                () -> assertThat(dto.message()).isEqualTo(contact.getMessage())
        );
    }

    @Test
    void itShouldThrownServerExceptionWhenAppWantsToSendContactMessage() {
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


        MailException mailExceptionMock = mock(MailException.class);
        doThrow(mailExceptionMock).when(javaMailSender).send((SimpleMailMessage) any());
        when(contactRepository.save(any())).thenReturn(contact);
        // When
        // Then
        assertThrows(ServerException.class, () -> underTest.sendContactMessage(contactDto));
    }

    @Test
    void itShouldCreateAndSendRegistrationConfirmationEmail() {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("email@test.pl")
                .role("CUSTOMER")
                .build();

        doNothing().when(javaMailSender).send((SimpleMailMessage) any());

        // When
        underTest.createAndSendRegistrationConfirmationEmail(userDto, "https://localhost:8008/1/17263082394");
        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Then
        verify(javaMailSender, times(1)).send(simpleMailMessageArgumentCaptor.capture());
    }

    @Test
    void itShouldThrowServerExceptionWhenAppWantsToCreateAndSendRegistrationConfirmationEmail() {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("email@test.pl")
                .role("CUSTOMER")
                .build();

        MailException mailExceptionMock = mock(MailException.class);
        doThrow(mailExceptionMock).when(javaMailSender).send((SimpleMailMessage) any());

        // When
        // Then
        assertThrows(ServerException.class, () -> underTest.createAndSendRegistrationConfirmationEmail(userDto, "https://localhost:8008/1/17263082394"));
        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender, times(1)).send(simpleMailMessageArgumentCaptor.capture());
    }
}