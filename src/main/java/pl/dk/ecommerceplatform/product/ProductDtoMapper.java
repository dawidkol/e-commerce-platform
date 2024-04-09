package pl.dk.ecommerceplatform.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.brand.BrandRepository;
import pl.dk.ecommerceplatform.category.CategoryRepository;
import pl.dk.ecommerceplatform.error.exceptions.brand.BrandNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.category.CategoryNotFoundException;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;
import pl.dk.ecommerceplatform.productImage.ImageFileData;
import pl.dk.ecommerceplatform.productImage.ImageFileDataRepository;
import pl.dk.ecommerceplatform.warehouse.Item;
import pl.dk.ecommerceplatform.warehouse.WarehouseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import static pl.dk.ecommerceplatform.constant.ProductConstant.DEFAULT_AVAILABILITY;
import static pl.dk.ecommerceplatform.constant.ProductConstant.DEFAULT_QUANTITY;

@Service
@AllArgsConstructor
public class ProductDtoMapper {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final WarehouseRepository warehouseRepository;
    private final ImageFileDataRepository imageFileDataRepository;

    public Product map(SaveProductDto saveProductDto) {
        return Product.builder()
                .id(saveProductDto.id())
                .name(saveProductDto.name())
                .description(saveProductDto.description())
                .price(saveProductDto.price())
                .category(categoryRepository.findById(saveProductDto.categoryId()).orElseThrow(CategoryNotFoundException::new))
                .brand(brandRepository.findById(saveProductDto.brandId()).orElseThrow(BrandNotFoundException::new))
                .added(LocalDate.now())
                .promotionPrice(saveProductDto.promotionPrice())
                .build();
    }

    public ProductDto map(Product product) {
        List<Long> imageIds = imageFileDataRepository.findAllByProduct_id(product.getId())
                .stream()
                .map(ImageFileData::getId)
                .toList();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(getPropertyValue(product, Item::getQuantity, DEFAULT_QUANTITY))
                .category(product.getCategory().getName())
                .brand(product.getBrand().getName())
                .available(getPropertyValue(product, Item::isAvailable, DEFAULT_AVAILABILITY))
                .added(product.getAdded())
                .promotionPrice(product.getPromotionPrice())
                .imageIds(imageIds)
                .build();
    }

    private <T> T getPropertyValue(Product product, Function<Item, T> extractor, T defaultValue) {
        return warehouseRepository.findByProduct_id(product.getId())
                .map(extractor)
                .orElseGet(() -> defaultValue);
    }
}
