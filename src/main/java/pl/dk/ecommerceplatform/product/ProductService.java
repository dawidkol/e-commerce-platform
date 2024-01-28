package pl.dk.ecommerceplatform.product;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductExistsException;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction;

@Service
@AllArgsConstructor
class ProductService {

    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper;

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


}
