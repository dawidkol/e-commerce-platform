package pl.dk.ecommerceplatform.warehouse;

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
class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @NotNull
    @PositiveOrZero
    private Long quantity;
    @NotNull
    private boolean available;
}
