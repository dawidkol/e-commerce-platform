package pl.dk.ecommerceplatform.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.product.Product;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> product = new ArrayList<>();

}
