package pl.dk.ecommerceplatform.product;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    private Boolean available;
    private LocalDate added;

}
