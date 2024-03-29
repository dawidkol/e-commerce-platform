package pl.dk.ecommerceplatform.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.brand.Brand;
import pl.dk.ecommerceplatform.category.Category;
import pl.dk.ecommerceplatform.productImage.ImageFileData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@PromotionConstraint
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    @NotBlank
    @Size(min = 10, max = 3000)
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    @NotNull
    private Brand brand;
    @NotNull
    private LocalDate added;
    @PositiveOrZero
    private BigDecimal promotionPrice;

}
