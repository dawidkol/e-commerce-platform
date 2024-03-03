package pl.dk.ecommerceplatform.email.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateEmailDto(@NotBlank String sender, @NotBlank String subject, @NotBlank String message) {
}
