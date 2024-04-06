package pl.dk.ecommerceplatform.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.address.Address;
import pl.dk.ecommerceplatform.address.AddressRepository;
import pl.dk.ecommerceplatform.cart.Cart;
import pl.dk.ecommerceplatform.cart.CartDtoMapper;
import pl.dk.ecommerceplatform.cart.CartRepository;
import pl.dk.ecommerceplatform.currency.Currency;
import pl.dk.ecommerceplatform.currency.CurrencyCode;
import pl.dk.ecommerceplatform.currency.CurrencyRepository;
import pl.dk.ecommerceplatform.error.exceptions.cart.CartNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.shipping.ShippingNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.OrderValueDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.shipping.Shipping;
import pl.dk.ecommerceplatform.shipping.ShippingRepository;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderDtoMapperTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ShippingRepository shippingRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CartDtoMapper cartDtoMapper;
    @Mock
    private CurrencyRepository currencyRepository;

    private OrderDtoMapper underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new OrderDtoMapper(userRepository, cartRepository, shippingRepository, addressRepository, cartDtoMapper, currencyRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldMapOrderDtoWithValidInputs() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        Shipping shipping = Shipping
                .builder()
                .id(shippingId)
                .shippingCost(BigDecimal.valueOf(10.99))
                .build();
        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();
        Cart cart = Cart.builder()
                .id(1L)
                .build();
        User user = new User();
        Address address = new Address();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.of(cart));
        when(shippingRepository.findById(saveOrderDto.shippingId())).thenReturn(Optional.of(shipping));
        when(addressRepository.findById(saveOrderDto.addressId())).thenReturn(Optional.of(address));

        // When
        underTest.map(userId, saveOrderDto);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
        verify(shippingRepository, times(1)).findById(saveOrderDto.shippingId());
        verify(addressRepository, times(1)).findById(saveOrderDto.addressId());
    }

    @Test
    void itShouldMapToOrderDto() {
        // Given
        User user = new User();
        BigDecimal shippingCost = BigDecimal.valueOf(10.01);
        BigDecimal cartValue = BigDecimal.valueOf(99.99);
        BigDecimal totalCost = cartValue.add(shippingCost);

        Shipping shipping = Shipping.builder()
                .name("DHL")
                .shippingCost(shippingCost)
                .build();
        Cart cart = Cart.builder()
                .id(1L)
                .build();
        Order order = Order.builder()
                .id(1L)
                .status(OrderStatus.NEW)
                .user(user)
                .cart(cart)
                .shipping(shipping)
                .address(new Address())
                .orderValue(cartValue)
                .created(LocalDateTime.now().minusDays(1))
                .build();

        when(cartDtoMapper.getCartValue(cart)).thenReturn(cartValue);

        // When
        OrderDto orderDto = underTest.map(order);

        // Then
        assertAll(
                () -> verify(cartDtoMapper, times(1)).getCartValue(cart),
                () -> assertThat(orderDto.shippingCost()).isEqualTo(shippingCost),
                () -> assertThat(orderDto.cartValue()).isEqualTo(cartValue),
                () -> assertThat(orderDto.totalCost()).isEqualTo(totalCost)
        );
    }

    @Test
    void itShouldThrowUserNotFoundException() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(UserNotFoundException.class, () -> underTest.map(userId, saveOrderDto));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void itShouldThrowCartNotFoundException() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CartNotFoundException.class, () -> underTest.map(userId, saveOrderDto));
        verify(userRepository, times(1)).findById(userId);
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
    }

    @Test
    void itShouldThrowShippingNotFoundException() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();

        User user = new User();
        Cart cart = new Cart();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.of(cart));
        when(shippingRepository.findById(saveOrderDto.shippingId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ShippingNotFoundException.class, () -> underTest.map(userId, saveOrderDto));
        verify(userRepository, times(1)).findById(userId);
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
        verify(shippingRepository, times(1)).findById(saveOrderDto.shippingId());
    }

    @Test
    void itShouldMapToOrderValueDto() {
        // Given
        Currency currency = Currency.builder()
                .id(1L)
                .name("euro")
                .code(CurrencyCode.EUR)
                .effectiveDate(LocalDate.of(2024, 4, 5))
                .ask(BigDecimal.valueOf(4.33))
                .ask(BigDecimal.valueOf(4.25))
                .build();

        Order order = Order.builder()
                .id(1L)
                .status(OrderStatus.NEW)
                .address(new Address())
                .orderValue(BigDecimal.valueOf(1000))
                .created(LocalDateTime.now().minusDays(1))
                .build();

        when(currencyRepository.findByCode(CurrencyCode.EUR)).thenReturn(Optional.of(currency));

        // When
        OrderValueDto orderValueDto = underTest.map(order, CurrencyCode.EUR);

        // Then
        assertAll(
                () -> assertThat(orderValueDto.id()).isEqualTo(order.getId()),
                () -> assertThat(orderValueDto.currencyCode()).isEqualTo(CurrencyCode.EUR),
                () -> assertThat(orderValueDto.orderValue()).isLessThan(order.getOrderValue())
        );
    }
}