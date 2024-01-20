package pl.dk.ecommerceplatform.product;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.lang.Nullable;
import pl.dk.ecommerceplatform.brand.Brand;
import pl.dk.ecommerceplatform.category.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 200)
    private String name;
    @NotBlank
    @Size(min = 10, max = 3000)
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    @NotNull
    @PositiveOrZero
    private Long quantity;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    @NotNull
    private Brand brand;
    @NotNull
    private Boolean available;
    @NotNull
    private LocalDate added;

}
