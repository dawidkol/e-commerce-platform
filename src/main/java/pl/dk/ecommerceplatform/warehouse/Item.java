package pl.dk.ecommerceplatform.warehouse;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.product.Product;


@Entity
@Table(name = "warehouse")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
    @NotNull
    @PositiveOrZero
    private Long quantity;
    @NotNull
    private boolean available;
}
