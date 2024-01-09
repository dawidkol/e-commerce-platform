package pl.dk.ecommerceplatform.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.brand.Brand;
import pl.dk.ecommerceplatform.category.Category;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long quantity;
//    private Long category_id;
//    private Long brand_id;
    private Boolean available;
    private LocalDate added;





}
