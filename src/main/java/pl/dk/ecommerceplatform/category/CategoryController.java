package pl.dk.ecommerceplatform.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;

import java.util.List;

import static pl.dk.ecommerceplatform.constant.PaginationConstant.PAGE_DEFAULT;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.SIZE_DEFAULT;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@RequestParam String name,
                                                                  @RequestParam(required = false, defaultValue = PAGE_DEFAULT) int page,
                                                                  @RequestParam(required = false, defaultValue = SIZE_DEFAULT) int size) {
        List<ProductDto> productsByCategory = categoryService.getProductsByCategory(name, page, size);
        return ResponseEntity.ok(productsByCategory);
    }
}
