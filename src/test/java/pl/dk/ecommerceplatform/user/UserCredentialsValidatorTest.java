package pl.dk.ecommerceplatform.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dk.ecommerceplatform.user.dtos.LoginUserDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCredentialsValidatorTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserCredentialsValidator underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserCredentialsValidator(userRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();;
    }

    @Test
    void itShouldReturnTrue() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();

        User user = User.builder()
                .email(loginUserDto.email())
                .password("rawPassword")
                .build();

        when(userRepository.findByEmail(loginUserDto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginUserDto.password(), user.getPassword())).thenReturn(true);

        // When
        boolean test = underTest.test(loginUserDto);

        // Then
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
        verify(passwordEncoder, times(1)).matches(loginUserDto.password(), user.getPassword());
        assertTrue(test);
    }

    @Test
    void itShouldReturnFalseWhenEmailNotExists() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();

        when(userRepository.findByEmail(loginUserDto.email())).thenReturn(Optional.empty());

        // When
        boolean test = underTest.test(loginUserDto);

        // Then
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
        assertFalse(test);
    }

    @Test
    void itShouldReturnFalseWhenPasswordNotMatches() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();

        User user = User.builder()
                .email(loginUserDto.email())
                .password("validPasssword")
                .build();

        when(userRepository.findByEmail(loginUserDto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginUserDto.password(), user.getPassword())).thenReturn(false);

        // When
        boolean test = underTest.test(loginUserDto);

        // Then
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
        verify(passwordEncoder, times(1)).matches(loginUserDto.password(), user.getPassword());
        assertFalse(test);
    }
}