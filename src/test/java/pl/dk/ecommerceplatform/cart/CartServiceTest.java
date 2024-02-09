package pl.dk.ecommerceplatform.cart;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.cart.dtos.AddToCartDto;
import pl.dk.ecommerceplatform.cart.dtos.CartDto;
import pl.dk.ecommerceplatform.error.exceptions.cart.CartNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.ProductUnavailableException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.QuantityException;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;
import pl.dk.ecommerceplatform.user.UserRole;
import pl.dk.ecommerceplatform.warehouse.Item;
import pl.dk.ecommerceplatform.warehouse.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartDtoMapper cartDtoMapper;
    @Mock
    private CartProductsDAO cartProductsDAO;
    private CartService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CartService(cartRepository, warehouseRepository, productRepository, userRepository, cartDtoMapper, cartProductsDAO);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldAddProductToCart() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Long productQuantity = 1L;
        AddToCartDto dto = AddToCartDto.builder()
                .productId(productId)
                .quantity(productQuantity)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();

        Item item = Item.builder()
                .id(1L)
                .quantity(20L)
                .available(true)
                .build();

        User user = User.builder()
                .id(userId)
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .products(new ArrayList<Product>())
                .user(user)
                .used(false)
                .build();

        CartDto cartDtoMock = mock(CartDto.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(userId)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct_id(productId)).thenReturn(Optional.of(item));
        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.of(cart));
        when(cartDtoMapper.map(cart)).thenReturn(cartDtoMock);


        // When
        underTest.addProductToCart(userId, dto);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(userId);
        verify(warehouseRepository, times(1)).findByProduct_id(productId);
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
        verify(cartDtoMapper, times(1)).map(cart);
    }

    @Test
    void itShouldThrowProductUnavailableException() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Long productQuantity = 1L;
        AddToCartDto dto = AddToCartDto.builder()
                .productId(productId)
                .quantity(productQuantity)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();

        Item item = Item.builder()
                .id(1L)
                .quantity(20L)
                .available(false)
                .build();

        User user = User.builder()
                .id(userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(userId)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct_id(productId)).thenReturn(Optional.of(item));

        // When
        // Then
        assertThrows(ProductUnavailableException.class, () -> underTest.addProductToCart(userId, dto));
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(userId);
        verify(warehouseRepository, times(1)).findByProduct_id(productId);
    }

    @Test
    void itShouldThrowUserNotFoundException() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Long productQuantity = 1L;
        AddToCartDto dto = AddToCartDto.builder()
                .productId(productId)
                .quantity(productQuantity)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(UserNotFoundException.class, () -> underTest.addProductToCart(userId, dto));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void itShouldThrowQuantityException() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Long productQuantity = 2L;
        AddToCartDto dto = AddToCartDto.builder()
                .productId(productId)
                .quantity(productQuantity)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();

        Item item = Item.builder()
                .id(1L)
                .quantity(1L)
                .available(true)
                .build();

        User user = User.builder()
                .id(userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(userId)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct_id(productId)).thenReturn(Optional.of(item));

        // When
        // Then
        assertThrows(QuantityException.class, () -> underTest.addProductToCart(userId, dto));
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(userId);
        verify(warehouseRepository, times(1)).findByProduct_id(productId);
    }

    @Test
    void itShouldUpdateProductQuantityInCart() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Long productQuantity = 2L;

        User user = User.builder()
                .id(userId)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();
        List<Product> productsList = List.of(product);

        Cart cart = Cart.builder()
                .id(1L)
                .products(productsList)
                .user(user)
                .used(false)
                .build();

        AddToCartDto dto = AddToCartDto.builder()
                .productId(productId)
                .quantity(productQuantity)
                .build();

        Item item = Item.builder()
                .id(1L)
                .quantity(20L)
                .available(true)
                .build();

        when(cartRepository.findByUser_id(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct_id(productId)).thenReturn(Optional.of(item));
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // When
        underTest.updateProductQuantityInCart(userId, dto);

        // Then
        verify(cartRepository, times(1)).findByUser_id(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(warehouseRepository, times(1)).findByProduct_id(productId);
        verify(cartProductsDAO, times(2)).insertProductToCart(longArgumentCaptor.capture(), longArgumentCaptor.capture());
    }

    @Test
    void itShouldCreateAndReturnNewCartWhenCartNotFoundForUser() {
        // Given
        String email = "john.doe@example.com";
        String password = "testPassword";
        String fistName = "John";
        String lastName = "Doe";
        Long id = 1L;

        UserRole customerRole = UserRole.builder()
                .id(id)
                .name("CUSTOMER")
                .description("description").build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        Cart cart = Cart.builder()
                .products(new ArrayList<>())
                .user(user)
                .used(false)
                .build();

        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(id)).thenReturn(Optional.empty());
        when(cartRepository.save(cart)).thenReturn(cart);
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // When
        underTest.getCart(user);

        // Then
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userIdArgumentCaptor.capture());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void itShouldDeleteUserCartIfPresent() {
        // Given
        Long userId = 1L;
        Cart cartMock = mock(Cart.class);
        when(cartRepository.findByUser_id(userId)).thenReturn(Optional.of(cartMock));

        // When
        underTest.cleanUserCart(userId);

        // Then
        verify(cartRepository, times(1)).findByUser_id(userId);
        verify(cartRepository, times(1)).delete(cartMock);
    }
}