package pl.dk.ecommerceplatform.order.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.websocket.OnOpen;
import lombok.Builder;

@Builder
public record UpdateOrderStatusDto(
        @NotNull @Positive Long orderId,
        @NotBlank String status) {
}
