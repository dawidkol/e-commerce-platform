package pl.dk.ecommerceplatform.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.brand.BrandRepository;
import pl.dk.ecommerceplatform.category.CategoryRepository;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;

import java.time.LocalDate;

@Service
@AllArgsConstructor
class ProductDtoMapper {


    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public Product map(SaveProductDto saveProductDto) {
        return Product.builder()
                .name(saveProductDto.name())
                .description(saveProductDto.description())
                .price(saveProductDto.price())
                .quantity(saveProductDto.quantity())
                .category(categoryRepository.findById(saveProductDto.categoryId()).orElseThrow())
                .brand(brandRepository.findById(saveProductDto.brandId()).orElseThrow())
                .available(saveProductDto.available())
                .added(LocalDate.now())
                .build();
    }

    public ProductDto map(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory().getName())
                .brand(product.getBrand().getName())
                .available(product.getAvailable())
                .added(product.getAdded())
                .build();
    }
}
