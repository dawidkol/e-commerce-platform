package pl.dk.ecommerceplatform.shipping;

import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

class ShippingDtoMapper {

    public static ShippingDto map(Shipping shipping) {
        return ShippingDto.builder()
                .id(shipping.getId())
                .name(shipping.getName().name())
                .shippingCost(shipping.getShippingCost())
                .build();
    }
}
