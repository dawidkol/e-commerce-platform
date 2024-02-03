package pl.dk.ecommerceplatform.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserExistsException;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static pl.dk.ecommerceplatform.constant.UserRoleConstant.CUSTOMER_ROLE;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    private UserDtoMapper userDtoMapper;
    @Mock
    private UtilsService utils;
    private UserService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userDtoMapper = new UserDtoMapper();
        underTest = new UserService(userRepository, userRoleRepository, userDtoMapper, utils);
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
                .name("CUSTOMER")
                .description("description").build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        when(userRoleRepository.findByName(CUSTOMER_ROLE)).thenReturn(Optional.of(customerRole));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        UserDto userDto = underTest.register(registerUserDto);

        // Then
        assertAll(
                () -> assertThat(userDto.id()).isEqualTo(id),
                () -> assertThat(userDto.firstName()).isEqualTo(fistName),
                () -> assertThat(userDto.lastName()).isEqualTo(lastName),
                () -> assertThat(userDto.email()).isEqualTo(email),
                () -> assertThat(userDto.role()).isEqualTo(customerRole.getName())
        );
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
                .name("CUSTOMER")
                .description("description").build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        when(userRoleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(UserExistsException.class, () -> underTest.register(registerUserDto));
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
                .name("CUSTOMER")
                .description("description").build();

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

        when(userRoleRepository.findByName(CUSTOMER_ROLE)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(userWithRole);

        // When
        UserDto userDto = underTest.register(registerUserDto);

        // Then
        assertAll(
                () -> assertThat(userDto.id()).isEqualTo(id),
                () -> assertThat(userDto.firstName()).isEqualTo(fistName),
                () -> assertThat(userDto.lastName()).isEqualTo(lastName),
                () -> assertThat(userDto.email()).isEqualTo(email),
                () -> assertThat(userDto.role()).isEqualTo(customerRole.getName())
        );
    }

    @Test
    void itShouldUpdateUser() throws JsonProcessingException, JsonPatchException {
        // Given
        Long userId = 1L;
        JsonMergePatch patch = mock(JsonMergePatch.class);
        User existingUser = new User();
        RegisterUserDto updatedUserDto = RegisterUserDto.builder()
                .firstName("newName")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(utils.applyPatch(existingUser, patch, RegisterUserDto.class))
                .thenReturn(updatedUserDto);

        // When
        underTest.updateUser(userId, patch);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(utils, times(1)).applyPatch(existingUser, patch, RegisterUserDto.class);
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
}