package pl.dk.ecommerceplatform.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.category.dtos.CategoryDto;
import pl.dk.ecommerceplatform.category.dtos.SaveCategoryDto;
import pl.dk.ecommerceplatform.error.exceptions.category.CategoryExistsException;
import pl.dk.ecommerceplatform.error.exceptions.category.CategoryNotFoundException;
import pl.dk.ecommerceplatform.product.ProductDtoMapper;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
class CategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDtoMapper productDtoMapper;
    private final CategoryDtoMapper categoryDtoMapper;

    public List<ProductDto> getProductsByCategory(String categoryName, int pageNumber, int size) {
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(CategoryNotFoundException::new);
        return productRepository.findAllByCategory_Name(category.getName(), PageRequest.of(pageNumber, size))
                .stream()
                .map(productDtoMapper::map)
                .toList();
    }

    @Transactional
    public CategoryDto saveCategory(SaveCategoryDto saveCategoryDto) {
        Optional<Category> category = categoryRepository.findByNameIgnoreCase(saveCategoryDto.name());
        if (category.isPresent()) {
            throw new CategoryExistsException();
        } else {
            Category categoryToSave = categoryDtoMapper.map(saveCategoryDto);
            Category savedCategory = categoryRepository.save(categoryToSave);
            return categoryDtoMapper.map(savedCategory);
        }
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryDtoMapper::map)
                .toList();
    }
}
