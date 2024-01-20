package pl.dk.ecommerceplatform.product;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;

import java.net.URI;
import java.util.List;

import static pl.dk.ecommerceplatform.product.ProductService.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductDto> saveProduct(@Valid @RequestBody SaveProductDto saveProductDto) {
        ProductDto dto = productService.saveProduct(saveProductDto);
        URI productUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(productUri).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(required = false, defaultValue = "10") Integer size,
                                                        @RequestParam(required = false, defaultValue = NAME) String field,
                                                        @RequestParam(required = false, defaultValue = "ASC") Direction dir) {
        return ResponseEntity.ok(productService.getProducts(page, size, field, dir));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getProductsByNameAndCategory(@RequestParam String name,
                                                                         @RequestParam(required = false) String category) {
        List<ProductDto> productsByName = productService.getProductsByNameAndCategory(name, category);
        if (!productsByName.isEmpty())
            return ResponseEntity.ok(productsByName);
        else return ResponseEntity.noContent().build();
    }

}
