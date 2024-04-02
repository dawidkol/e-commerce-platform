package pl.dk.ecommerceplatform.shipping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.shipping.ShippingMethodExistException;
import pl.dk.ecommerceplatform.error.exceptions.shipping.ShippingNotFoundException;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
class ShippingService {

    private final ShippingRepository shippingRepository;

    public ShippingDto updateShippingCost(Long id, BigDecimal newPrice) {
        Shipping shipping = shippingRepository.findById(id).orElseThrow(ShippingNotFoundException::new);
        shipping.setShippingCost(newPrice);
        return ShippingDtoMapper.map(shipping);
    }

    public ShippingDto saveShippingMethod(ShippingDto shippingDto) {
        shippingRepository.findByName(shippingDto.name())
                .ifPresent(shipping -> {
                            throw new ShippingMethodExistException("Shipping method with name %s exists"
                                    .formatted(shipping.getName()));
                            }
                );
        Shipping shipping = ShippingDtoMapper.map(shippingDto);
        Shipping savedShipping = shippingRepository.save(shipping);
        return ShippingDtoMapper.map(savedShipping);
    }
}
