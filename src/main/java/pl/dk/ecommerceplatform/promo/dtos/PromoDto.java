package pl.dk.ecommerceplatform.promo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record PromoDto(Long id,
                       String code,
                       @NotNull @Min(1) @Max(100)
                       Long discountPercent,
                       @DateTimeFormat @NotNull
                       LocalDateTime activeStart,
                       @DateTimeFormat @NotNull
                       LocalDateTime activeEnd,
                       Boolean active,
                       @Min(1)
                       Long usageCount,
                       @Min(1)
                       Long maxUsageCount) {
}
