package pl.dk.ecommerceplatform.productImage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.product.Product;

@Entity
@Table(name = "image_file_data")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ImageFileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String filePatch;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
