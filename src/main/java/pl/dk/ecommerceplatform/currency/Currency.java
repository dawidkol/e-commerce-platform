package pl.dk.ecommerceplatform.currency;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(name = "currency")
@Data
@Builder
@AllArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private CurrencyCode code;
    private LocalDate effectiveDate;
    private BigDecimal bid;
    private BigDecimal ask;
}
