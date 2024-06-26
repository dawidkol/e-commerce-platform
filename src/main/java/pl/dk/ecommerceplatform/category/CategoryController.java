package pl.dk.ecommerceplatform.category;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.category.dtos.CategoryDto;
import pl.dk.ecommerceplatform.category.dtos.SaveCategoryDto;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;

import java.net.URI;
import java.util.List;

import static pl.dk.ecommerceplatform.constant.PaginationConstant.PAGE_DEFAULT;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.SIZE_DEFAULT;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{name}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable String name,
                                                                  @RequestParam(required = false, defaultValue = PAGE_DEFAULT) int page,
                                                                  @RequestParam(required = false, defaultValue = SIZE_DEFAULT) int size) {
        List<ProductDto> productsByCategory = categoryService.getProductsByCategory(name, page, size);
        return ResponseEntity.ok(productsByCategory);
    }

    @PostMapping("")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody SaveCategoryDto saveCategoryDto) {
        CategoryDto categoryDto = categoryService.saveCategory(saveCategoryDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(categoryDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getCategories();
        return ResponseEntity.ok(categoryDtoList);
    }

}
