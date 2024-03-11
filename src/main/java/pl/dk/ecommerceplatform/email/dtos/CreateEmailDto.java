package pl.dk.ecommerceplatform.email.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateEmailDto(@NotBlank @Email String sender, @NotBlank String subject, @NotBlank String message) {
}
