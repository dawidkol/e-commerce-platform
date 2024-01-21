package pl.dk.ecommerceplatform.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterUserDto(Long id,
                              @NotBlank
                              @Size(min = 3, max = 75)
                              String firstName,
                              @NotBlank
                              @Size(min = 3, max = 75)
                              String lastName,
                              @Email
                              @Size(min = 6, max = 75)
                              String email,
                              @NotBlank
                              @Size(min = 6, max = 75)
                              String password) {
}
