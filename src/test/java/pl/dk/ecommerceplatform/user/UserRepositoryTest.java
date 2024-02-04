package pl.dk.ecommerceplatform.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "default")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldReturnUserByGivenEmail() {
        // Given
        String email = "janusz.kowalski@test.pl";

        // When
        Optional<User> optionalUser = underTest.findByEmail(email);

        // Then
        assertThat(optionalUser).isPresent().hasValueSatisfying(
                user -> {
                    assertThat(user.getEmail()).isEqualTo(email);
                    assertThat(user.getId()).isEqualTo(1L);
                    assertThat(user.getFirstName()).isEqualTo("Janusz");
                    assertThat(user.getLastName()).isEqualTo("Kowalski");
                }
        );
    }

    @Test
    void itShouldReturnEmptyOptionalWhenGivenEmail() {
        // Given
        String email = "abc@test.pl";

        // When
        Optional<User> optionalUser = underTest.findByEmail(email);

        // Then
        assertThat(optionalUser).isNotPresent();
    }
}