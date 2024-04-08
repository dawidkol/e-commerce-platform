package pl.dk.ecommerceplatform.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.dk.ecommerceplatform.cart.Cart;
import pl.dk.ecommerceplatform.cart.CartRepository;
import pl.dk.ecommerceplatform.currency.CurrencyCode;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderStatusNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.ItemNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.QuantityException;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.OrderValueDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.promo.Promo;
import pl.dk.ecommerceplatform.promo.PromoRepository;
import pl.dk.ecommerceplatform.warehouse.Item;
import pl.dk.ecommerceplatform.warehouse.WarehouseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDtoMapper orderDtoMapper;
    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private PromoRepository promoRepository;

    private OrderService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new OrderServiceImpl(orderRepository, orderDtoMapper, warehouseRepository, cartRepository, promoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldCreateOrder() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();

        Product product1 = Product.builder().id(1L).build();
        Product product2 = Product.builder().id(2L).build();
        List<Product> productList = List.of(product1, product1, product2, product2, product2);

        Item item1 = Item.builder()
                .id(1L)
                .product(product1)
                .quantity(100L)
                .available(true)
                .build();

        Item item2 = Item.builder()
                .id(2L)
                .product(product2)
                .quantity(100L)
                .available(true)
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .products(productList)
                .used(false)
                .build();

        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.of(cart));
        when(warehouseRepository.findByProduct_id(1L)).thenReturn(Optional.of(item1));
        when(warehouseRepository.findByProduct_id(2L)).thenReturn(Optional.of(item2));

        // When
        underTest.createOrder(userId, saveOrderDto);

        // Then
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
        verify(warehouseRepository, times(1)).findByProduct_id(1L);
        verify(warehouseRepository, times(1)).findByProduct_id(2L);
    }

    @Test
    void itShouldThrowQuantityException() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();

        Product product1 = Product.builder().id(1L).build();
        Product product2 = Product.builder().id(2L).build();
        List<Product> productList = List.of(product1, product1, product2, product2, product2);

        Item item1 = Item.builder()
                .id(1L)
                .product(product1)
                .quantity(1L)
                .available(true)
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .products(productList)
                .used(false)
                .build();

        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.of(cart));
        when(warehouseRepository.findByProduct_id(1L)).thenReturn(Optional.of(item1));

        // When
        // Then
        assertThrows(QuantityException.class, () -> underTest.createOrder(userId, saveOrderDto));
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
        verify(warehouseRepository, times(1)).findByProduct_id(1L);
    }

    @Test
    void itShouldThrowItemNotFoundException() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();

        Product product1 = Product.builder().id(1L).build();
        Product product2 = Product.builder().id(2L).build();
        List<Product> productList = List.of(product1, product1, product2, product2, product2);

        Cart cart = Cart.builder()
                .id(1L)
                .products(productList)
                .used(false)
                .build();

        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.of(cart));
        when(warehouseRepository.findByProduct_id(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ItemNotFoundException.class, () -> underTest.createOrder(userId, saveOrderDto));
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
        verify(warehouseRepository, times(1)).findByProduct_id(1L);
    }

    @Test
    void itShouldUpdateOrderStatus() {
        // Given
        Long orderId = 1L;
        UpdateOrderStatusDto orderStatusDto = UpdateOrderStatusDto.builder()
                .status("PAID")
                .build();

        Order order = Order.builder()
                .id(1L)
                .build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        underTest.updateOrderStatus(orderId, orderStatusDto);

        // Then
        verify(orderRepository, times(1)).findById(orderId);

    }

    @Test
    void itShouldThrowOrderNotFoundExceptionWhenUserTryToUpdateStatus() {
        // Given
        Long orderId = 1L;
        UpdateOrderStatusDto orderStatusDto = UpdateOrderStatusDto.builder()
                .status("PAID")
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(OrderNotFoundException.class, () -> underTest.updateOrderStatus(orderId, orderStatusDto));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void itShouldThrowOrderStatusNotFoundExceptionWhenUserWantsToSetOrderThatNotExists() {
        // Given
        Long orderId = 1L;
        UpdateOrderStatusDto orderStatusDto = UpdateOrderStatusDto.builder()
                .status("WRONG STATUS")
                .build();

        Order order = Order.builder()
                .id(1L)
                .build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        // Then
        assertThrows(OrderStatusNotFoundException.class, () -> underTest.updateOrderStatus(orderId, orderStatusDto));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void itShouldRetrieveOrderWhenUserHasAdminRole() {
        // Given
        Long userId = 1L;
        Long orderId = 1L;
        List<String> credentials = List.of("ROLE_ADMIN");

        Order order = Order.builder()
                .id(orderId)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderDtoMapper.map(order)).thenReturn((orderDto));

        // When
        underTest.getOrder(credentials, orderId, userId);

        // Then
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDtoMapper, times(1)).map(order);
    }

    @Test
    void itShouldRetrieveOrderWhenUserHasCustomerRole() {
        // Given
        Long userId = 1L;
        Long orderId = 1L;
        List<String> credentials = List.of("ROLE_CUSTOMER");

        Order order = Order.builder()
                .id(orderId)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .build();

        when(orderRepository.findByIdAndUser_id(orderId, userId)).thenReturn(Optional.of(order));
        when(orderDtoMapper.map(order)).thenReturn((orderDto));

        // When
        underTest.getOrder(credentials, orderId, userId);

        // Then
        verify(orderRepository, times(1)).findByIdAndUser_id(orderId, userId);
        verify(orderDtoMapper, times(1)).map(order);
    }

    @Test
    void itShouldRetrieveAllOffersWhenUserHasAdminRole() {
        // Given
        Long userId = 1L;
        Long orderId = 1L;
        List<String> credentials = List.of("ROLE_ADMIN");

        Order order = Order.builder()
                .id(orderId)
                .build();

        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Order> orderList = List.of(order);
        PageImpl<Order> orders = new PageImpl<>(orderList);

        when(orderRepository.findAll(pageRequest)).thenReturn(orders);
        // When

        underTest.getOrders(credentials, userId, page, size);

        // Then
        verify(orderRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void itShouldRetrieveAllUserOffersWhenUserHasCustomerRole() {
        // Given
        Long userId = 1L;
        Long orderId = 1L;
        List<String> credentials = List.of("ROLE_CUSTOMER");

        Order order = Order.builder()
                .id(orderId)
                .build();

        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Order> orderList = List.of(order);

        when(orderRepository.findAllByUser_id(userId, pageRequest)).thenReturn(orderList);
        // When

        underTest.getOrders(credentials, userId, page, size);

        // Then
        verify(orderRepository, times(1)).findAllByUser_id(userId, pageRequest);
    }

    @Test
    void itShouldCreateOrderWithDiscount() {
        // Given
        Long userId = 1L;
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .promoCode("testPromoCode")
                .build();

        Product product1 = Product.builder().id(1L).build();
        Product product2 = Product.builder().id(2L).build();
        List<Product> productList = List.of(product1, product1, product2, product2, product2);

        Item item1 = Item.builder()
                .id(1L)
                .product(product1)
                .quantity(100L)
                .available(true)
                .build();

        Item item2 = Item.builder()
                .id(2L)
                .product(product2)
                .quantity(100L)
                .available(true)
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .products(productList)
                .used(false)
                .build();

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Promo promo = Promo.builder()
                .id(1L)
                .code("testPromoCode")
                .discountPercent(5L)
                .activeStart(start)
                .activeEnd(end)
                .active(true)
                .usageCount(1L)
                .maxUsageCount(5L)
                .build();

        Order order = Order.builder()
                .id(1L)
                .orderValue(BigDecimal.valueOf(199))
                .build();

        when(cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)).thenReturn(Optional.of(cart));
        when(warehouseRepository.findByProduct_id(1L)).thenReturn(Optional.of(item1));
        when(warehouseRepository.findByProduct_id(2L)).thenReturn(Optional.of(item2));
        when(promoRepository.findByCode(saveOrderDto.promoCode())).thenReturn(Optional.of(promo));
        when(orderDtoMapper.map(any(Long.class), any(SaveOrderDto.class))).thenReturn(order);

        // When
        underTest.createOrder(userId, saveOrderDto);

        // Then
        verify(cartRepository, times(1)).findCartByUserIdWhereUsedEqualsFalse(userId);
        verify(warehouseRepository, times(1)).findByProduct_id(1L);
        verify(warehouseRepository, times(1)).findByProduct_id(2L);
        verify(promoRepository, times(1)).findByCode(saveOrderDto.promoCode());
        verify(orderDtoMapper, times(1)).map(any(Long.class), any(SaveOrderDto.class));
    }

    @Test
    void itShouldCalculateOrderValueWithEurCurrency() {
        // Given
        Long orderId = 1L;

        Order order = Order.builder()
                .id(orderId)
                .orderValue(BigDecimal.valueOf(1000))
                .build();

        OrderValueDto orderValueDto = OrderValueDto.builder()
                .id(1L)
                .currencyCode(CurrencyCode.EUR)
                .orderValue(order.getOrderValue().multiply(BigDecimal.valueOf(0.4)))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderDtoMapper.map(order, CurrencyCode.EUR)).thenReturn(orderValueDto);
        String currencyCodeString = CurrencyCode.EUR.name();

        // When
        underTest.calculateOrderValueWithOtherCurrency(orderId, currencyCodeString);

        // Then
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDtoMapper, times(1)).map(order, CurrencyCode.EUR);
    }
}