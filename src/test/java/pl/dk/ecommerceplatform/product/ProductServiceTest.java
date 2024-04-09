package pl.dk.ecommerceplatform.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import pl.dk.ecommerceplatform.brand.Brand;
import pl.dk.ecommerceplatform.category.Category;
import pl.dk.ecommerceplatform.constant.PaginationConstant;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductExistsException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductDtoMapper productDtoMapper;
    private ProductService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private UtilsService utilsService;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
//        utilsService = new UtilsService(new ObjectMapper());
        underTest = new ProductService(productRepository, productDtoMapper, utilsService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSaveProduct() {
        // Given
        String name = "Test productName";
        String description = "Test description";
        Category category = Category.builder().id(1L).build();
        Brand brand = Brand.builder().id(1L).build();
        LocalDate added = LocalDate.now();
        BigDecimal price = new BigDecimal("99.99");

        SaveProductDto saveProductDto = SaveProductDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .categoryId(1L)
                .brandId(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .brand(brand)
                .added(added)
                .build();

        when(productDtoMapper.map(saveProductDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productDtoMapper.map(product)).thenReturn(mock(ProductDto.class));

        // When
        underTest.saveProduct(saveProductDto);

        // Then
        assertAll(
                () -> verify(productDtoMapper, times(1)).map(saveProductDto),
                () -> verify(productRepository, times(1)).save(product),
                () -> verify(productDtoMapper, times(1)).map(product)
        );
    }

    @Test
    void itShouldThrowProductExistsExceptionWhenProductExists() {
        // Given
        String name = "Test productName";
        String description = "Test description";
        Category category = Category.builder().id(1L).build();
        Brand brand = Brand.builder().id(1L).build();
        LocalDate added = LocalDate.now();
        BigDecimal price = new BigDecimal("99.99");

        SaveProductDto saveProductDto = SaveProductDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .categoryId(1L)
                .brandId(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .brand(brand)
                .added(added)
                .build();

        when(productRepository.findByName(name)).thenReturn(Optional.of(product));

        // When
        // Then
        assertThrows(ProductExistsException.class, () -> underTest.saveProduct(saveProductDto));
        verify(productRepository, times(1)).findByName(name);
    }

    @Test
    void itShouldReturnOptionalProductDtoByGivenId() {
        // Given
        Long productId = 1L;
        String name = "Test productName";
        String description = "Test description";
        Category category = Category.builder().id(1L).build();
        Brand brand = Brand.builder().id(1L).build();
        LocalDate added = LocalDate.now();
        BigDecimal price = new BigDecimal("99.99");
        Product product = Product.builder()
                .id(productId)
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .brand(brand)
                .added(added)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productDtoMapper.map(product)).thenReturn(mock(ProductDto.class));

        // When
        underTest.getProductById(productId);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(productDtoMapper, times(1)).map(product);
    }

    @Test
    void itShouldReturnEmptyOptionalWhenGivenIdWitchNotExistsInDb() {
        // Given
        Long productId = 1L;
        String name = "Test productName";
        String description = "Test description";
        Category category = Category.builder().id(1L).build();
        Brand brand = Brand.builder().id(1L).build();
        LocalDate added = LocalDate.now();
        BigDecimal price = new BigDecimal("99.99");
        Product product = Product.builder()
                .id(productId)
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .brand(brand)
                .added(added)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(productDtoMapper.map(product)).thenReturn(mock(ProductDto.class));

        // When
        Optional<ProductDto> productDtoOptional = underTest.getProductById(productId);

        // Then
        verify(productRepository, times(1)).findById(productId);
        assertThat(productDtoOptional).isEmpty();
    }

    @Test
    void itShouldGetAllSortedProductsByGivenPropertyIdDescending() {
        // Given
        Integer page = Integer.valueOf(PaginationConstant.PAGE_DEFAULT);
        Integer size = Integer.valueOf(PaginationConstant.SIZE_DEFAULT);
        String property = "id";
        Sort.Direction direction = Sort.Direction.DESC;
        ProductDto productDtoMock = mock(ProductDto.class);
        Product productMock = mock(Product.class);

        PageRequest pageRequest = PageRequest.of(page, size).withSort(direction, property);
        PageImpl<Product> products = new PageImpl<>(List.of(productMock));

        when(productRepository.findAll(pageRequest)).thenReturn(products);
        when(productDtoMapper.map(productMock)).thenReturn(productDtoMock);

        // When
        underTest.getProducts(page, size, property, direction);

        // Then
        verify(productRepository, times(1)).findAll(pageRequest);
        verify(productDtoMapper, times(1)).map(productMock);
    }

    @Test
    void itShouldGetAllProductsByGivenNameAndCategory() {
        // Given
        String name = "Apple";
        String category = "Electronics";
        ProductDto productDtoMock = mock(ProductDto.class);
        Product productMock = mock(Product.class);

        when(productRepository.findByNameAndCategory(name, category)).thenReturn(List.of(productMock));
        when(productDtoMapper.map(productMock)).thenReturn(productDtoMock);

        // When
        underTest.getProductsByNameAndCategory(name, category);

        // Then
        verify(productRepository, times(1)).findByNameAndCategory(name, category);
        verify(productDtoMapper, times(1)).map(productMock);
    }

    @Test
    void itShouldGetAllProductsByGivenNameAndCategoryEqualsNull() {
        // Given
        String name = "Apple";
        String category = null;
        ProductDto productDtoMock = mock(ProductDto.class);
        Product productMock = mock(Product.class);

        when(productRepository.findAllByName(name)).thenReturn(List.of(productMock));
        when(productDtoMapper.map(productMock)).thenReturn(productDtoMock);
        // When

        underTest.getProductsByNameAndCategory(name, category);

        // Then
        verify(productRepository, times(1)).findAllByName(name);
        verify(productDtoMapper, times(1)).map(productMock);
    }

    @Test
    void itShouldGetAllPromotionProducts() {
        // Given
        int page = 0;
        int size = 1;

        Product product1 = Product.builder().price(BigDecimal.valueOf(100.00)).promotionPrice(BigDecimal.valueOf(99.99)).build();
        Product product2 = Product.builder().price(BigDecimal.valueOf(200.00)).promotionPrice(BigDecimal.valueOf(129.99)).build();
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Product> productList = List.of(product1, product2);
        when(productRepository.findAllPromotionProducts(pageRequest)).thenReturn(productList);

        // When
        List<ProductDto> allProductPromotion = underTest.getAllProductPromotion(page, size);

        //Then
        verify(productRepository, times(1)).findAllPromotionProducts(pageRequest);
        assertThat(allProductPromotion.size()).isEqualTo(productList.size());
    }

    @Test
    void itShouldUpdateProduct() throws JsonPatchException, JsonProcessingException {
        // Given
        Long productId = 1L;
        JsonMergePatch jsonMergePatchMock = mock(JsonMergePatch.class);

        SaveProductDto saveProductDto = SaveProductDto.builder()
                .id(productId)
                .name("Product Name")
                .description("Product Description")
                .brandId(1L)
                .categoryId(1L)
                .price(BigDecimal.valueOf(120.00))
                .promotionPrice(BigDecimal.valueOf(100.00))
                .build();

        Product product = Product.builder()
                .id(productId)
                .name("Product Name")
                .description("Product Description")
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .price(BigDecimal.valueOf(120.00))
                .promotionPrice(BigDecimal.valueOf(100.00))
                .build();

        ProductDto productDto =  ProductDto.builder()
                .id(productId)
                .name("Product Name")
                .description("Product Description")
                .price(BigDecimal.valueOf(120.00))
                .quantity(1L)
                .category("Electronics")
                .brand("newBrandance")
                .available(true)
                .added(LocalDate.now().minusMonths(1))
                .imageIds(Collections.emptyList())
                .promotionPrice(BigDecimal.valueOf(100.00))
                .build();


        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productDtoMapper.map(product)).thenReturn(productDto);
        when(utilsService.applyPatch(saveProductDto, jsonMergePatchMock, SaveProductDto.class)).thenReturn(saveProductDto);
        when(productDtoMapper.map(saveProductDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        // When
        underTest.updateProduct(productId, jsonMergePatchMock);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(productDtoMapper, times(1)).map(product);
        verify(utilsService, times(1)).applyPatch(saveProductDto, jsonMergePatchMock, SaveProductDto.class);
        verify(productDtoMapper, times(1)).map(saveProductDto);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void itShouldThrowJsonPatchExceptionWhenUpdateProduct() throws JsonPatchException, JsonProcessingException {
        // Given
        Long productId = 1L;
        JsonMergePatch jsonMergePatchMock = mock(JsonMergePatch.class);

        SaveProductDto saveProductDto = SaveProductDto.builder()
                .id(productId)
                .name("Product Name")
                .description("Product Description")
                .brandId(1L)
                .categoryId(1L)
                .price(BigDecimal.valueOf(120.00))
                .promotionPrice(BigDecimal.valueOf(100.00))
                .build();

        Product product = Product.builder()
                .id(productId)
                .name("Product Name")
                .description("Product Description")
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .price(BigDecimal.valueOf(120.00))
                .promotionPrice(BigDecimal.valueOf(100.00))
                .build();

        ProductDto productDto =  ProductDto.builder()
                .id(productId)
                .name("Product Name")
                .description("Product Description")
                .price(BigDecimal.valueOf(120.00))
                .quantity(1L)
                .category("Electronics")
                .brand("newBrandance")
                .available(true)
                .added(LocalDate.now().minusMonths(1))
                .imageIds(Collections.emptyList())
                .promotionPrice(BigDecimal.valueOf(100.00))
                .build();


        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productDtoMapper.map(product)).thenReturn(productDto);
        when(utilsService.applyPatch(saveProductDto, jsonMergePatchMock, SaveProductDto.class)).thenThrow(JsonPatchException.class);

        // When
        // Then
        assertThrows(ServerException.class, () -> underTest.updateProduct(productId, jsonMergePatchMock));
        verify(productRepository, times(1)).findById(productId);
        verify(productDtoMapper, times(1)).map(product);
        verify(utilsService, times(1)).applyPatch(saveProductDto, jsonMergePatchMock, SaveProductDto.class);
    }
}