package pl.dk.ecommerceplatform.confirmationToken;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.error.exceptions.token.TokenNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.dk.ecommerceplatform.constant.TokenConstant.VALID_TIME;

class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    private TokenService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new TokenServiceImpl(tokenRepository, userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldGenerateConformationToken() {
        // Given
        String email = "john.doe@test.pl";

        User user = User.builder()
                .id(1L)
                .active(false)
                .email(email)
                .build();
        Token token = Token.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusMinutes(VALID_TIME))
                .user(User.builder().id(1L).build()).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.save(any())).thenReturn(token);
        // When

        TokenDto tokenByUserEmail = underTest.generateConfirmationToken(email);
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);
        // Then

        verify(userRepository, times(1)).findByEmail(email);
        verify(tokenRepository, times(1)).save(tokenArgumentCaptor.capture());

        assertAll(
                () -> assertThat(tokenByUserEmail.userId()).isEqualTo(token.getUser().getId()),
                () -> assertThat(tokenByUserEmail.id()).isEqualTo(token.getId()),
                () -> assertEquals(tokenByUserEmail.expiration(), token.getExpiration()),
                () -> assertEquals(tokenByUserEmail.token(), token.getToken())
        );
    }

    @Test
    void itShouldThrowUserNotFoundExceptionWhenUserEmailNotExists() {
        // Given
        String email = "john.doe@test.pl";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(UserNotFoundException.class, () -> underTest.generateConfirmationToken(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void itShouldReturnTokenDtoByGivenTokenString() {
        // Given
        String tokenString = UUID.randomUUID().toString();
        Token token = Token.builder()
                .id(1L)
                .token(tokenString)
                .expiration(LocalDateTime.now().plusMinutes(VALID_TIME))
                .user(User.builder().id(1L).build()).build();

        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.of(token));

        // When
        TokenDto tokenDto = underTest.getToken(tokenString);

        // Then
        verify(tokenRepository, times(1)).findByToken(tokenString);
        assertEquals(token.getId(), tokenDto.id());
        assertEquals(token.getToken(), tokenDto.token());
        assertEquals(token.getUser().getId(), tokenDto.userId());
        assertEquals(token.getExpiration(), tokenDto.expiration());
    }

    @Test
    void itShouldThrowTokenNotFoundExceptionWhenGivenTokenStringNotExists() {
        // Given
        String tokenString = UUID.randomUUID().toString();
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(TokenNotFoundException.class, () -> underTest.getToken(tokenString));
        verify(tokenRepository, times(1)).findByToken(tokenString);
    }

    @Test
    void itShouldReturnTokenDtoByGivenUserEmail() {
        // Given
        String userEmail = "john.doe@test.pl";
        Token token = Token.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusMinutes(VALID_TIME))
                .user(User.builder().id(1L).email(userEmail).build()).build();

        when(tokenRepository.findTokenByUser_Email(userEmail)).thenReturn(Optional.of(token));

        // When
        TokenDto tokenDto = underTest.getTokenByUserEmail(userEmail);

        // Then
        verify(tokenRepository, times(1)).findTokenByUser_Email(userEmail);
        assertEquals(token.getId(), tokenDto.id());
        assertEquals(token.getToken(), tokenDto.token());
        assertEquals(token.getUser().getId(), tokenDto.userId());
        assertEquals(token.getExpiration(), tokenDto.expiration());
    }

    @Test
    void itShouldThrowTokenNotFoundExceptionWhenTokenNotExistsForUser() {
        // Given
        String userEmail = "john.doe@test.pl";
        when(tokenRepository.findTokenByUser_Email(userEmail)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(TokenNotFoundException.class, () -> underTest.getTokenByUserEmail(userEmail));
        verify(tokenRepository, times(1)).findTokenByUser_Email(userEmail);
    }

    @Test
    void itShouldDeleteAllInactiveTokens() throws InterruptedException {
        // Given
        when(tokenRepository.findAllExpiredTokens()).thenReturn(List.of(mock(Token.class), mock(Token.class), mock(Token.class)));

        // When
        underTest.cleanInactiveTokens();

        // Then
        verify(tokenRepository, times(1)).findAllExpiredTokens();
        verify(tokenRepository, times(1)).deleteAll(any());
    }

    @Test
    void itShouldDeleteAllInactiveNoTokensToDelete() throws InterruptedException {
        // Given
        when(tokenRepository.findAllExpiredTokens()).thenReturn(Collections.emptyList());

        // When
        underTest.cleanInactiveTokens();

        // Then
        verify(tokenRepository, times(1)).findAllExpiredTokens();
    }
}