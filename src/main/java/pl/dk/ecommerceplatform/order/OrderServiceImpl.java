package pl.dk.ecommerceplatform.order;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.cart.Cart;
import pl.dk.ecommerceplatform.cart.CartRepository;
import pl.dk.ecommerceplatform.currency.CurrencyCode;
import pl.dk.ecommerceplatform.currency.CurrencyService;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderStatusNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.promo.PromoCodeExpiredException;
import pl.dk.ecommerceplatform.error.exceptions.promo.PromoCodeNotActiveException;
import pl.dk.ecommerceplatform.error.exceptions.promo.PromoCodeNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.promo.PromoCodeUsedException;
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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static pl.dk.ecommerceplatform.utils.UtilsService.getLogger;
import static pl.dk.ecommerceplatform.utils.UtilsService.isAdmin;

@Service
@AllArgsConstructor
class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderDtoMapper;
    private final WarehouseRepository warehouseRepository;
    private final CartRepository cartRepository;
    private final PromoRepository promoRepository;
    private final Logger logger = getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, SaveOrderDto saveOrderDto) {
        Map<Long, Long> productsMap = this.getProductsMap(userId);
        this.checkProductAvailability(productsMap);
        Order orderToSave = orderDtoMapper.map(userId, saveOrderDto);
        DiscountResult result = new DiscountResult(0L, BigDecimal.ZERO);
        if (saveOrderDto.promoCode() != null) {
            result = getDiscountResult(saveOrderDto, orderToSave);
        }
        this.updateWarehouse(productsMap);
        Order savedOrder = orderRepository.save(orderToSave);
        return orderDtoMapper.map(savedOrder, result.discountPercent(), result.discountValue());
    }

    @Override
    @Transactional
    public void updateOrderStatus(UpdateOrderStatusDto updateOrderStatusDto) {
        Long orderId = updateOrderStatusDto.orderId();
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        OrderStatus newStatus;
        try {
            String orderUpperCase = updateOrderStatusDto.status().toUpperCase();
            newStatus = Enum.valueOf(OrderStatus.class, orderUpperCase);
        } catch (IllegalArgumentException e) {
            throw new OrderStatusNotFoundException();
        }
        order.setStatus(newStatus);
    }

    @Override
    public OrderDto getOrder(List<String> credentials, Long orderId, Long userId) {
        boolean isAdmin = isAdmin(credentials);
        if (isAdmin) {
            return orderRepository.findById(orderId)
                    .map(orderDtoMapper::map)
                    .orElseThrow(OrderNotFoundException::new);
        } else {
            return orderRepository.findByIdAndUser_id(orderId, userId)
                    .map(orderDtoMapper::map)
                    .orElseThrow(OrderNotFoundException::new);
        }
    }

    @Override
    public List<OrderDto> getOrders(List<String> credentials, Long userId, int page, int size) {
        List<OrderDto> orders;
        boolean isAdmin = isAdmin(credentials);
        if (isAdmin) {
            orders = this.getAllOrders(page, size);
        } else {
            orders = this.getUserOrders(userId, page, size);
        }
        return orders;
    }

    private List<OrderDto> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(orderDtoMapper::map)
                .toList();
    }

    private List<OrderDto> getUserOrders(Long userId, int page, int size) {
        return orderRepository.findAllByUser_id(userId, PageRequest.of(page, size))
                .stream()
                .map(orderDtoMapper::map)
                .toList();
    }

    private void updateWarehouse(Map<Long, Long> products) {
        logger.error(products.toString());

        Iterator<Map.Entry<Long, Long>> iterator = products.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Long> next = iterator.next();
            warehouseRepository.updateQuantity(next.getKey(), next.getValue());
        }
    }

    private void checkProductAvailability(Map<Long, Long> products) {
        logger.error(products.toString());

        Iterator<Map.Entry<Long, Long>> iterator = products.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Long> next = iterator.next();
            Long productQuantity = warehouseRepository.findByProduct_id(next.getKey())
                    .map(Item::getQuantity)
                    .orElseThrow(ItemNotFoundException::new);
            if (productQuantity < next.getValue())
                throw new QuantityException("Insufficient stock of the product in the warehouse. Product id = %d".formatted(next.getKey()));
        }
    }

    private Map<Long, Long> getProductsMap(Long userId) {
        return cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)
                .stream()
                .map(Cart::getProducts)
                .flatMap(Collection::stream)
                .map(Product::getId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private DiscountResult getDiscountResult(SaveOrderDto saveOrderDto, Order order) {
        Long discountPercent = 0L;
        BigDecimal discountValue = BigDecimal.ZERO;
        BigDecimal totalCost = order.getOrderValue();

        discountPercent = this.getDiscountPercent(saveOrderDto.promoCode());
        discountValue = this.calculateOrderValueAfterDiscount(discountPercent, order);
        totalCost = totalCost.subtract(discountValue);
        order.setOrderValue(totalCost);

        return new DiscountResult(discountPercent, discountValue);
    }

    private BigDecimal calculateOrderValueAfterDiscount(Long discount, Order order) {
        BigDecimal currentOrderValue = order.getOrderValue();
        BigDecimal discountPercent = BigDecimal.valueOf(discount * 0.01);
        return currentOrderValue.multiply(discountPercent).setScale(2, RoundingMode.HALF_UP);
    }

    private Long getDiscountPercent(String promoCode) {
        Promo promo = promoRepository.findByCode(promoCode).orElseThrow(PromoCodeNotFoundException::new);
        this.validPromoCode(promo);
        return promo.getDiscountPercent();
    }

    private void validPromoCode(Promo promo) {
        LocalDateTime now = LocalDateTime.now();
        if (promo.getActive().equals(false)) throw new PromoCodeNotActiveException();
        if (promo.getActiveEnd().isBefore(now)) throw new PromoCodeExpiredException();
        if (promo.getUsageCount() >= promo.getMaxUsageCount()) throw new PromoCodeUsedException();
    }

    private record DiscountResult(Long discountPercent, BigDecimal discountValue) {
    }

    @Override
    public OrderValueDto calculateOrderValueWithAnotherValue(Long orderId, String code) {
        CurrencyCode currencyCode = CurrencyCode.getCurrency(code);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return orderDtoMapper.map(order, currencyCode);
    }
}
