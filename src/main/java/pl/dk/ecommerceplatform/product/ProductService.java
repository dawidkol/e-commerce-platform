package pl.dk.ecommerceplatform.product;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.*;

@Service
@AllArgsConstructor
class ProductService {

    public static final String PRICE_ASC = "price_asc";
    public static final String PRICE_DESC = "price_desc";
    public static final String PRICE = "price";
    public static final String NAME = "name";
    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper;

    public ProductDto save(SaveProductDto saveProductDto) {
        Product productToSave = productDtoMapper.map(saveProductDto);
        Product savedProduct = productRepository.save(productToSave);
        return productDtoMapper.map(savedProduct);
    }

    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productDtoMapper::map);
    }

    public List<ProductDto> getProducts(int pageNumber, int size, String sortBy) {
        if (sortBy.equals(PRICE_ASC) && pageNumber >= 0 && size >= 0) {
            return getSortedProducts(pageNumber, size, PRICE, Direction.ASC);
        } else if (sortBy.equals(PRICE_DESC) && pageNumber >= 0 && size >= 0) {
            return getSortedProducts(pageNumber, size, PRICE, Direction.DESC);
        } else if (sortBy.isEmpty())
            return productRepository.findAll(Sort.by(NAME)).stream().map(productDtoMapper::map).toList();
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public List<ProductDto> getProductsByNameAndCategory(String name, String category) {
        if (category == null) {
            return productRepository.findByName(name)
                    .stream().map(productDtoMapper::map)
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
