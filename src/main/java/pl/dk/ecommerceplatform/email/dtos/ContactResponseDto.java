package pl.dk.ecommerceplatform.email.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactResponseDto(@NotNull Long contactId, @NotBlank @Size(min = 10) String response) {
}
