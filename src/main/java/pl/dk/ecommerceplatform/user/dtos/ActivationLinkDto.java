package pl.dk.ecommerceplatform.user.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ActivationLinkDto(LocalDateTime expirationTime) {
}
