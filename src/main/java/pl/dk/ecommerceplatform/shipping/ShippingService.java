package pl.dk.ecommerceplatform.shipping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.shipping.ShippingNotFoundException;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
class ShippingService {

    private final ShippingRepository shippingRepository;

    public ShippingDto updateShippingCost(Long id, BigDecimal newPrice) {
        Shipping shipping = shippingRepository.findById(id).orElseThrow(ShippingNotFoundException::new);
        shipping.setShippingCost(newPrice);
        return ShippingDtoMapper.map(shipping);
    }
}
