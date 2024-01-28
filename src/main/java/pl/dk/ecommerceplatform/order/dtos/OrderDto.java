package pl.dk.ecommerceplatform.order.dtos;

import lombok.Builder;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;
import pl.dk.ecommerceplatform.cart.dtos.CartProductDto;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderDto(Long id,
                       String status,
                       String username,
                       AddressDto shippingAddress,
                       List<CartProductDto> products,
                       BigDecimal cartValue,
                       BigDecimal shippingCost,
                       BigDecimal totalCost) {
}
