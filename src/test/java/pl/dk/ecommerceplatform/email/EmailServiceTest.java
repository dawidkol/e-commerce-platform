package pl.dk.ecommerceplatform.email;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.NestedRuntimeException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.dk.ecommerceplatform.email.dtos.CreateEmailDto;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    private EmailService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EmailServiceImpl(javaMailSender);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSendContactMessage() {
        // Given
        CreateEmailDto createEmailDto = mock(CreateEmailDto.class);
        when(createEmailDto.sender()).thenReturn("sender@example.com");
        when(createEmailDto.subject()).thenReturn("Test Subject");
        when(createEmailDto.message()).thenReturn("Test Message");

        // When
        doNothing().when(javaMailSender).send((SimpleMailMessage) any());

        // Then
        underTest.sendContactMessage(createEmailDto);
        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender, times(2)).send(simpleMailMessageArgumentCaptor.capture());
    }

    @Test
    void itShouldThrownServerExceptionWhenAppWantsToSendContactMessage() {
        // Given
        CreateEmailDto createEmailDto = mock(CreateEmailDto.class);
        when(createEmailDto.sender()).thenReturn("sender@example.com");
        when(createEmailDto.subject()).thenReturn("Test Subject");
        when(createEmailDto.message()).thenReturn("Test Message");

        MailException mailExceptionMock = mock(MailException.class);
        doThrow(mailExceptionMock).when(javaMailSender).send((SimpleMailMessage) any());

        // When
        // Then
        assertThrows(ServerException.class, () -> underTest.sendContactMessage(createEmailDto));
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