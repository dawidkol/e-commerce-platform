package pl.dk.ecommerceplatform.product;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;
import pl.dk.ecommerceplatform.review.ReviewService;
import pl.dk.ecommerceplatform.review.dtos.ReviewProductDto;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static pl.dk.ecommerceplatform.constant.PaginationConstant.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    @PostMapping("")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
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
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false, defaultValue = PAGE_DEFAULT) int page,
                                                        @RequestParam(required = false, defaultValue = SIZE_DEFAULT) int size,
                                                        @RequestParam(required = false, defaultValue = SORT_NAME) String property,
                                                        @RequestParam(required = false, defaultValue = ASC) Direction dir) {
        return ResponseEntity.ok(productService.getProducts(page, size, property, dir));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getProductsByNameAndCategory(@RequestParam String name,
                                                                         @RequestParam(required = false) String category) {
        List<ProductDto> productsByName = productService.getProductsByNameAndCategory(name, category);
        if (!productsByName.isEmpty())
            return ResponseEntity.ok(productsByName);
        else return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<ReviewProductDto> getAllProductReviews(@PathVariable Long id) {
        Optional<ProductDto> productDtoOptional = productService.getProductById(id);
        if (productDtoOptional.isPresent()) {
            ReviewProductDto allProductsReviews = reviewService.getAllProductsReviews(id);
            return ResponseEntity.ok(allProductsReviews);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
