package pl.dk.ecommerceplatform.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.ecommerceplatform.product.ProductDtoMapper;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
class CategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDtoMapper productDtoMapper;

    List<ProductDto> getProductsByCategory(String categoryName, int pageNumber, int size) {
        try {
            Category category = categoryRepository.findByNameIgnoreCase(categoryName).orElseThrow();
            return productRepository.findAllByCategory_Name(category.getName(), PageRequest.of(pageNumber, size))
                    .stream()
                    .map(productDtoMapper::map)
                    .toList();
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not exists");
        }
    }
}
