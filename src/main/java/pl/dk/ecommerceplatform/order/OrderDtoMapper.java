package pl.dk.ecommerceplatform.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.address.Address;
import pl.dk.ecommerceplatform.address.AddressRepository;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;
import pl.dk.ecommerceplatform.cart.Cart;
import pl.dk.ecommerceplatform.cart.CartDtoMapper;
import pl.dk.ecommerceplatform.cart.CartRepository;
import pl.dk.ecommerceplatform.cart.CartService;
import pl.dk.ecommerceplatform.error.exceptions.address.AddressNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.cart.CartNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.shipping.ShippingNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.shipping.Shipping;
import pl.dk.ecommerceplatform.shipping.ShippingMethod;
import pl.dk.ecommerceplatform.shipping.ShippingRepository;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
class OrderDtoMapper {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ShippingRepository shippingRepository;
    private final AddressRepository addressRepository;
    private final CartDtoMapper cartDtoMapper;

    public Order map(Long userId, SaveOrderDto saveOrderDto) {
        User user = this.getUser(userId);
        Cart cart = this.getCart(userId);
        Shipping shipping = this.getShipping(saveOrderDto.shippingId());
        Address address = this.getAddress(saveOrderDto.addressId());
        BigDecimal cartValue = cartDtoMapper.getCartValue(cart);
        cart.setUsed(true);
        return Order.builder()
                .status(OrderStatus.NEW)
                .user(user)
                .cart(cart)
                .shipping(shipping)
                .address(address)
                .orderValue(cartValue)
                .created(LocalDateTime.now())
                .build();
    }

    public OrderDto map(Order order, Long discountPercent, BigDecimal discountValue) {
        BigDecimal cartValue = cartDtoMapper.getCartValue(order.getCart());
        ShippingDto shippingDto = this.getShippingDto(order);
        BigDecimal shippingCost = shippingDto.shippingCost();
        BigDecimal totalOrderCost = order.getOrderValue().add(shippingCost);
        return OrderDto.builder()
                .id(order.getId())
                .username(order.getUser().getEmail())
                .status(order.getStatus().name())
                .shippingAddress(this.getAddressDto(order))
                .products(cartDtoMapper.getCartProductsDto(order.getCart()))
                .cartValue(cartValue)
                .discountPercent(discountPercent)
                .discountValue(discountValue)
                .shippingCost(shippingCost)
                .cartValueAfterDiscount(order.getOrderValue())
                .totalCost(totalOrderCost)
                .build();
    }

    public OrderDto map(Order order) {
        ShippingDto shippingDto = this.getShippingDto(order);
        BigDecimal shippingCost = shippingDto.shippingCost();

        BigDecimal totalOrderCost = order.getOrderValue().add(shippingCost);
        return OrderDto.builder()
                .id(order.getId())
                .username(order.getUser().getEmail())
                .status(order.getStatus().name())
                .shippingAddress(this.getAddressDto(order))
                .products(cartDtoMapper.getCartProductsDto(order.getCart()))
                .cartValue(order.getOrderValue())
                .shippingCost(shippingCost)
                .totalCost(totalOrderCost)
                .build();
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private Cart getCart(Long userId) {
        return cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)
                .orElseThrow(CartNotFoundException::new);
    }

    private Shipping getShipping(Long id) {
        return shippingRepository.findById(id)
                .orElseThrow(ShippingNotFoundException::new);
    }

    private Address getAddress(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);
    }

    private AddressDto getAddressDto(Order order) {
        Address address = order.getAddress();
        return AddressDto
                .builder()
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .buildingNumber(address.getBuildingNumber())
                .phoneNumber(address.getPhoneNumber())
                .build();
    }

    private ShippingDto getShippingDto(Order order) {
        Shipping shipping = order.getShipping();
        ShippingMethod shippingMethod = shipping.getName();
        return ShippingDto.builder()
                .id(shipping.getId())
                .name(shippingMethod.name())
                .shippingCost(shipping.getShippingCost())
                .build();
    }

}
