package pl.dk.ecommerceplatform.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductExistsException;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction;

@Service
@AllArgsConstructor
class ProductService {

    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper;
    private final UtilsService utilsService;

    @Transactional
    public ProductDto saveProduct(SaveProductDto saveProductDto) {
        Optional<Product> product = productRepository.findByName(saveProductDto.name());
        if (product.isPresent()) throw new ProductExistsException();
        Product productToSave = productDtoMapper.map(saveProductDto);
        Product savedProduct = productRepository.save(productToSave);
        return productDtoMapper.map(savedProduct);
    }

    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productDtoMapper::map);
    }

    public List<ProductDto> getProducts(int pageNumber, int size, String property, Direction direction) {
        return getSortedProducts(pageNumber, size, property, direction);
    }

    public List<ProductDto> getProductsByNameAndCategory(String name, String category) {
        if (category == null) {
            return productRepository.findAllByName(name)
                    .stream()
                    .map(productDtoMapper::map)
                    .toList();
        } else {
            return productRepository.findByNameAndCategory(name, category)
                    .stream()
                    .map(productDtoMapper::map)
                    .toList();
        }
    }

    private List<ProductDto> getSortedProducts(Integer pageNumber, Integer size, String sortBy, Direction direction) {
        return productRepository.findAll(PageRequest.of(pageNumber, size)
                        .withSort(direction, sortBy))
                .map(productDtoMapper::map)
                .getContent();
    }

    public List<ProductDto> getAllProductPromotion(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAllPromotionProducts(pageRequest)
                .stream()
                .map(productDtoMapper::map)
                .toList();
    }

    @Transactional
    public void updateProduct(Long id, JsonMergePatch jsonMergePatch) {
        SaveProductDto prepareProductDataToUpdate = this.prepareProductDataToUpdate(id);
        try {
            SaveProductDto saveProductDto = utilsService.applyPatch(prepareProductDataToUpdate, jsonMergePatch, SaveProductDto.class);
            Product productToUpdate = productDtoMapper.map(saveProductDto);
            productRepository.save(productToUpdate);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }

    private SaveProductDto prepareProductDataToUpdate(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(ProductNotFoundException::new);
        ProductDto productDto = productDtoMapper.map(product);
        return SaveProductDto.builder()
                .id(product.getId())
                .name(productDto.name())
                .description(productDto.description())
                .brandId(product.getBrand().getId())
                .categoryId(product.getCategory().getId())
                .price(productDto.price())
                .promotionPrice(productDto.promotionPrice())
                .build();
    }

}
