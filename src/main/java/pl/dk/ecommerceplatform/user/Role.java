package pl.dk.ecommerceplatform.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("Full authorities"),
    EMPLOYEE("Employee authorities"),
    CUSTOMER("Customer authorities");

    private final String description;
}
