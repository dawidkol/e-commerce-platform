package pl.dk.ecommerceplatform.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.dk.ecommerceplatform.user.Role.CUSTOMER;

@DataJpaTest
@ActiveProfiles(value = "test")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository underTest;

    @Test
    void itShouldName() {
        // Given
        // When
        Optional<UserRole> optionalUserRole = underTest.findByName(CUSTOMER.name());

        //Then
        assertThat(optionalUserRole).isPresent().hasValueSatisfying(
                userRole -> {
                    assertThat(userRole.getId()).isNotNull();
                    assertThat(userRole.getName()).isEqualTo(CUSTOMER.name());
                    assertThat(userRole.getDescription()).isEqualTo(CUSTOMER.getDescription());
                }
        );
    }
}