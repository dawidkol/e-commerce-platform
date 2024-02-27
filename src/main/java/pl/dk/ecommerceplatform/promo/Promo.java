package pl.dk.ecommerceplatform.promo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promo")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(min = 5, max = 50)
    private String code;
    @NotNull @Min(1) @Max(100)
    private Long discountPercent;
    @DateTimeFormat @NotNull
    private LocalDateTime activeStart;
    @DateTimeFormat @NotNull
    private LocalDateTime activeEnd;
    private Boolean active;
    @Min(0)
    private Long usageCount;
    @Min(1)
    private Long maxUsageCount;
}
