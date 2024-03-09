package pl.dk.ecommerceplatform.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dk.ecommerceplatform.confirmationToken.TokenService;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.error.exceptions.token.ActiveTokenExistsException;
import pl.dk.ecommerceplatform.error.exceptions.token.InvalidTokenException;
import pl.dk.ecommerceplatform.error.exceptions.token.TokenExpiredException;
import pl.dk.ecommerceplatform.error.exceptions.token.TokenNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.*;
import pl.dk.ecommerceplatform.user.dtos.LoginUserDto;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.user.dtos.UserTokenWrapper;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.dk.ecommerceplatform.user.Role.CUSTOMER;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserDtoMapper userDtoMapper;
    @Mock
    private UtilsService utils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserCredentialsValidator userCredentialsValidator;
    private UserService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserService(userRepository, userRoleRepository, userDtoMapper, utils, passwordEncoder, tokenService, userCredentialsValidator);
    }

    @AfterEach
    void afterAll() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldRegisterNewUser() {
        // Given
        String email = "john.doe@example.com";
        String password = "testPassword";
        String fistName = "John";
        String lastName = "Doe";
        Long id = 1L;

        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        UserRole customerRole = UserRole.builder()
                .id(id)
                .name(CUSTOMER.name())
                .description(CUSTOMER.getDescription()).build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        UserDto expectedUserDto = UserDto.builder().build();

        when(userRepository.findByEmail(registerUserDto.email())).thenReturn(Optional.empty());
        when(userDtoMapper.map(any(User.class))).thenReturn(expectedUserDto);
        when(userDtoMapper.map(any(RegisterUserDto.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserDto result = underTest.register(registerUserDto, CUSTOMER.name());

        // Then
        Assertions.assertNotNull(result);
        assertEquals(expectedUserDto, result);
        verify(userRepository, times(1)).findByEmail(registerUserDto.email());
        verify(userDtoMapper, times(1)).map(any(User.class));
        verify(userDtoMapper, times(1)).map(any(RegisterUserDto.class));
        verify(passwordEncoder, times(1)).encode(registerUserDto.password());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void itShouldThrowExceptionWhenUserExists() {
        // Given
        String email = "john.doe@example.com";
        String password = "testPassword";
        String fistName = "John";
        String lastName = "Doe";

        Long id = 1L;
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        UserRole customerRole = UserRole.builder()
                .id(id)
                .name(CUSTOMER.name())
                .description(CUSTOMER.getDescription()).build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        when(userRoleRepository.findByName(CUSTOMER.name())).thenReturn(Optional.of(customerRole));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(UserExistsException.class, () -> underTest.register(registerUserDto, CUSTOMER.name()));
    }

    @Test
    void itShouldSetCustomerRoleAndRegisterNewUser() {
        // Given
        String email = "john.doe@example.com";
        String password = "testPassword";
        String fistName = "John";
        String lastName = "Doe";
        Long id = 1L;

        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        UserRole customerRole = UserRole.builder()
                .id(id)
                .name(CUSTOMER.name())
                .description(CUSTOMER.getDescription()).build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        User userWithRole = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        UserDto expectedUserDto = UserDto.builder().build();

        when(userRoleRepository.findByName(CUSTOMER.name())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(userWithRole);
        when(userDtoMapper.map(any(User.class))).thenReturn(expectedUserDto);
        when(userDtoMapper.map(any(RegisterUserDto.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        underTest.register(registerUserDto, CUSTOMER.name());

        // Then
        verify(userRoleRepository, times(1)).findByName(CUSTOMER.name());
        verify(userRepository, times(1)).findByEmail(registerUserDto.email());
        verify(userDtoMapper, times(1)).map(any(User.class));
        verify(userDtoMapper, times(1)).map(any(RegisterUserDto.class));
        verify(passwordEncoder, times(1)).encode(registerUserDto.password());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void itShouldUpdateUser() throws JsonProcessingException, JsonPatchException {
        // Given
        Long userId = 1L;
        JsonMergePatch patch = mock(JsonMergePatch.class);
        UserRole customerRole = UserRole.builder()
                .id(1L)
                .name(CUSTOMER.name())
                .description(CUSTOMER.getDescription()).build();

        User user = User.builder()
                .password("password")
                .userRole(customerRole)
                .build();

        RegisterUserDto updatedUserDto = RegisterUserDto.builder()
                .firstName("newName")
                .build();

        when(utils.applyPatch(user, patch, RegisterUserDto.class))
                .thenReturn(updatedUserDto);
        when(userDtoMapper.map(any(RegisterUserDto.class))).thenReturn(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        underTest.updateUser(userId, patch);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(utils, times(1)).applyPatch(user, patch, RegisterUserDto.class);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void itShouldCatchJsonPatchException() throws JsonProcessingException, JsonPatchException {
        // Given
        Long userId = 1L;
        JsonMergePatch patch = mock(JsonMergePatch.class);
        User existingUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(utils.applyPatch(existingUser, patch, RegisterUserDto.class))
                .thenThrow(JsonPatchException.class);

        // When
        assertThrows(ServerException.class, () -> underTest.updateUser(userId, patch));
    }

    @Test
    void itShouldCatchJsonProcessingException() throws JsonProcessingException, JsonPatchException {
        // Given
        Long userId = 1L;
        JsonMergePatch patch = mock(JsonMergePatch.class);
        User existingUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(utils.applyPatch(existingUser, patch, RegisterUserDto.class))
                .thenThrow(JsonProcessingException.class);

        // When
        assertThrows(ServerException.class, () -> underTest.updateUser(userId, patch));
    }

    @Test
    void itShouldThrowRoleNotFoundException() {
        // Given
        RegisterUserDto registerUserDto = RegisterUserDto.builder().email("sample@emai.com").build();
        String notExistingUserRole = "notExistingUserRole";

        // When
        // Then
        assertThrows(RoleNotFoundException.class, () -> underTest.register(registerUserDto, notExistingUserRole));
    }

    @Test
    void itShouldDeleteUserByGivenEmail() {
        // Given
        String email = "sebastian.kowalski@test.pl";

        User user = User.builder()
                .id(2L)
                .firstName("Sebastian")
                .lastName("Kowalski")
                .email(email)
                .password("password")
                .build();


        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        underTest.deleteUser(email);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        // Then
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).delete(argumentCaptor.capture());
    }

    @Test
    void itShouldThrowUserNotFoundExceptionWhenAdminWantsToDeleteUser() {
        // Given
        String email = "sebastian.kowalski@test.pl";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(UserNotFoundException.class, () -> underTest.deleteUser(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void itShouldConfirmAccount() {
        // Given
        Long userId = 1L;
        String token = UUID.randomUUID().toString();

        TokenDto tokenDto = TokenDto.builder()
                .token(token)
                .userId(userId)
                .expiration(LocalDateTime.now().plusMinutes(15))
                .build();
        User user = User.builder().id(userId).build();
        when(tokenService.getToken(token)).thenReturn(tokenDto);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        underTest.confirmAccount(userId, token);

        // Then
        verify(tokenService, times(1)).getToken(token);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void itShouldThrowAccountAlreadyExceptionWhenUserWantsToConfirmToken() {
        // Given
        Long userId = 1L;
        String token = UUID.randomUUID().toString();

        TokenDto tokenDto = TokenDto.builder()
                .token(token)
                .userId(userId)
                .expiration(LocalDateTime.now().plusMinutes(15))
                .build();

        User user = User.builder()
                .id(userId)
                .active(true)
                .build();

        when(tokenService.getToken(token)).thenReturn(tokenDto);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(AccountAlreadyActivatedException.class, () -> underTest.confirmAccount(userId, token));
        verify(tokenService, times(1)).getToken(token);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void itShouldThrownTokenExpiredExceptionWhenUserWantsToConfirmToken() {
        // Given
        Long userId = 1L;
        String token = UUID.randomUUID().toString();

        TokenDto tokenDto = TokenDto.builder()
                .token(token)
                .userId(userId)
                .expiration(LocalDateTime.now().minusMinutes(15))
                .build();

        when(tokenService.getToken(token)).thenReturn(tokenDto);

        // When
        // Then
        assertThrows(TokenExpiredException.class, () -> underTest.confirmAccount(userId, token));
        verify(tokenService, times(1)).getToken(token);
    }

    @Test
    void itShouldThrownInvalidTokenExceptionWhenUserWantsToConfirmToken() {
        // Given
        Long userId = 1L;
        Long userTokenId = 2L;
        String token = UUID.randomUUID().toString();

        TokenDto tokenDto = TokenDto.builder()
                .token(token)
                .userId(userId)
                .expiration(LocalDateTime.now().plusMinutes(15))
                .build();

        when(tokenService.getToken(token)).thenReturn(tokenDto);

        // When
        // Then
        assertThrows(InvalidTokenException.class, () -> underTest.confirmAccount(userTokenId, token));
        verify(tokenService, times(1)).getToken(token);
    }

    @Test
    void itShouldCreateTokenSuccessfully() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();
        User user = User.builder()
                .id(1L)
                .email(loginUserDto.email())
                .build();

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        TokenDto tokenDto = TokenDto.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusMinutes(15))
                .userId(user.getId())
                .build();

        TokenDto tokenByUserId = TokenDto.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().minusMinutes(15))
                .userId(user.getId())
                .build();

        when(userCredentialsValidator.test(loginUserDto)).thenReturn(true);
        when(userRepository.findByEmail(loginUserDto.email())).thenReturn(Optional.of(user));
        when(userDtoMapper.map(user)).thenReturn(userDto);
        when(tokenService.generateConfirmationToken(userDto.email())).thenReturn(tokenDto);
        when(tokenService.getTokenByUserEmail(userDto.email())).thenReturn(tokenByUserId);

        // When
        UserTokenWrapper userTokenWrapper = underTest.createToken(loginUserDto);

        // Then
        verify(userCredentialsValidator, times(1)).test(loginUserDto);
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
        verify(userDtoMapper, times(1)).map(user);
        verify(tokenService, times(1)).generateConfirmationToken(userDto.email());
        verify(tokenService, times(1)).getTokenByUserEmail(userDto.email());
        assertThat(userTokenWrapper).isNotNull();
    }

    @Test
    void itShouldThrowUserCredentialExceptionWhenUserWantsToCreateTokenWithInvalidCredentials() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();

        when(userCredentialsValidator.test(loginUserDto)).thenReturn(false);

        // When
        assertThrows(UserCredentialException.class, () -> underTest.createToken(loginUserDto));

        // Then
        verify(userCredentialsValidator, times(1)).test(loginUserDto);
    }

    @Test
    void itShouldThrowAccountAlreadyExistsExceptionWhenUserWantsToGenerateToken() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();
        User user = User.builder()
                .id(1L)
                .email(loginUserDto.email())
                .active(true)
                .build();

        when(userCredentialsValidator.test(loginUserDto)).thenReturn(true);
        when(userRepository.findByEmail(loginUserDto.email())).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(AccountAlreadyActivatedException.class, () -> underTest.createToken(loginUserDto));
        verify(userCredentialsValidator, times(1)).test(loginUserDto);
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
    }

    @Test
    void itShouldThrowActiveTokenExistsExceptionWhenUserWantsToGenerateToken() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();
        User user = User.builder()
                .id(1L)
                .email(loginUserDto.email())
                .build();

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        TokenDto tokenByUserId = TokenDto.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusMinutes(15))
                .userId(user.getId())
                .build();

        when(userCredentialsValidator.test(loginUserDto)).thenReturn(true);
        when(userRepository.findByEmail(loginUserDto.email())).thenReturn(Optional.of(user));
        when(tokenService.getTokenByUserEmail(userDto.email())).thenReturn(tokenByUserId);

        // When
        assertThrows(ActiveTokenExistsException.class, () -> underTest.createToken(loginUserDto));

        // Then
        verify(userCredentialsValidator, times(1)).test(loginUserDto);
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
        verify(tokenService, times(1)).getTokenByUserEmail(userDto.email());
    }

    @Test
    void itShouldCreateTokenWhenGetTokenByUserEmailMethodThrowsTokenNotFoundException() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("john.doe@test.pl")
                .password("rawPassword")
                .build();
        User user = User.builder()
                .id(1L)
                .email(loginUserDto.email())
                .build();

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        TokenDto tokenDto = TokenDto.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusMinutes(15))
                .userId(user.getId())
                .build();

        when(userCredentialsValidator.test(loginUserDto)).thenReturn(true);
        when(userRepository.findByEmail(loginUserDto.email())).thenReturn(Optional.of(user));
        when(userDtoMapper.map(user)).thenReturn(userDto);
        when(tokenService.generateConfirmationToken(userDto.email())).thenReturn(tokenDto);
        doThrow(TokenNotFoundException.class).when(tokenService).getTokenByUserEmail(userDto.email());

        // When
        UserTokenWrapper userTokenWrapper = underTest.createToken(loginUserDto);

        // Then
        verify(userCredentialsValidator, times(1)).test(loginUserDto);
        verify(userRepository, times(1)).findByEmail(loginUserDto.email());
        verify(userDtoMapper, times(1)).map(user);
        verify(tokenService, times(1)).generateConfirmationToken(userDto.email());
        verify(tokenService, times(1)).getTokenByUserEmail(userDto.email());
        assertThat(userTokenWrapper).isNotNull();
    }

}