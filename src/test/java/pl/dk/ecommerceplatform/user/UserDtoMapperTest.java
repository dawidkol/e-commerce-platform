package pl.dk.ecommerceplatform.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.constant.UserRoleConstant;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.dk.ecommerceplatform.constant.UserRoleConstant.CUSTOMER_ROLE;
import static pl.dk.ecommerceplatform.constant.UserRoleConstant.CUSTOMER_ROLE_DESCRIPTION;

class UserDtoMapperTest {

    private UserDtoMapper underTest;

    @BeforeEach
    void init() {
        underTest = new UserDtoMapper();
    }

    @Test
    void itShouldMapToUserDto() {
        // Given
        String email = "john.doe@example.com";
        String password = "testPassword";
        String fistName = "John";
        String lastName = "Doe";
        Long id = 1L;

        UserRole customerRole = UserRole.builder()
                .id(id)
                .name(CUSTOMER_ROLE)
                .description(CUSTOMER_ROLE_DESCRIPTION).build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        // When
        UserDto userDto = underTest.map(user);

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
    void itShouldMapToUser() {
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

        // When
        User user = underTest.map(registerUserDto);

        // Then
        assertAll(
                () -> assertThat(user.getId()).isEqualTo(id),
                () -> assertThat(user.getFirstName()).isEqualTo(fistName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(user.getEmail()).isEqualTo(email),
                () -> assertThat(user.getPassword()).isNotEmpty()
        );
    }
}