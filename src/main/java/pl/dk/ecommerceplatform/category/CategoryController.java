package pl.dk.ecommerceplatform.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;

import java.util.List;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@RequestParam String name,
                                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        List<ProductDto> productsByCategory = categoryService.getProductsByCategory(name, page, size);
        return ResponseEntity.ok(productsByCategory);
    }
}
