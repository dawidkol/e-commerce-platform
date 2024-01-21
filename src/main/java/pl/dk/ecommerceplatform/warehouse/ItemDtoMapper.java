package pl.dk.ecommerceplatform.warehouse;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.warehouse.dtos.ItemDto;
import pl.dk.ecommerceplatform.warehouse.dtos.SaveItemDto;

@Service
class ItemDtoMapper {

    public Item map(SaveItemDto saveItemDto, Product product) {
        return Item.builder()
                .product(product)
                .quantity(saveItemDto.quantity())
                .available(saveItemDto.available())
                .build();
    }

    public ItemDto map(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .available(item.isAvailable())
                .build();
    }
}
