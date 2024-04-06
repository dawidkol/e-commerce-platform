package pl.dk.ecommerceplatform.shipping;

import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

class ShippingDtoMapper {

    public static ShippingDto map(Shipping shipping) {
        return ShippingDto.builder()
                .id(shipping.getId())
                .name(shipping.getName())
                .shippingCost(shipping.getShippingCost())
                .build();
    }

    public static Shipping map(ShippingDto shippingDto) {
        return Shipping.builder()
                .id(shippingDto.id())
                .name(shippingDto.name())
                .shippingCost(shippingDto.shippingCost())
                .build();
    }
}
