package pl.dk.ecommerceplatform.promo.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record SavePromoDto(Long id,
                           @NotBlank @Size(min = 5, max = 50)
                           String code,
                           @NotNull @Min(1) @Max(100)
                           Long discountPercent,
                           @DateTimeFormat @NotNull
                           LocalDateTime activeStart,
                           @DateTimeFormat @NotNull
                           LocalDateTime activeEnd,
                           Boolean active,
                           @Min(1)
                           Long maxUsageCount) {
}

