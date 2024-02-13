package pl.dk.ecommerceplatform.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.warehouse.dtos.ItemDto;
import pl.dk.ecommerceplatform.warehouse.dtos.SaveItemDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemDtoMapperTest {

    private ItemDtoMapper underTest;

    @BeforeEach
    void init() {
        underTest = new ItemDtoMapper();
    }

    @Test
    void itShouldMapToItem() {
        // Given
        Long productId = 1L;
        Long quantity = 10L;
        boolean available = true;

        SaveItemDto saveItemDto = SaveItemDto.builder()
                .productId(productId)
                .quantity(quantity)
                .available(available)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();
        // When
        Item item = underTest.map(saveItemDto, product);

        // Then
        assertAll(
                () -> assertThat(item.getProduct()).isEqualTo(product),
                () -> assertThat(item.getQuantity()).isEqualTo(quantity),
                () -> assertThat(item.isAvailable()).isEqualTo(available)
        );
    }

    @Test
    void itShouldMapToItemDto() {
        // Given
        Long productId = 1L;
        Long quantity = 10L;
        boolean available = true;

        Product product = Product.builder()
                .id(productId)
                .build();

        Long itemId = 1L;
        Item item = Item.builder()
                .id(itemId)
                .product(product)
                .quantity(quantity)
                .available(available)
                .build();
        // When
        ItemDto itemDto = underTest.map(item);

        // Then
        assertAll(
                () -> assertThat(itemDto.id()).isEqualTo(itemId),
                () -> assertThat(itemDto.productName()).isEqualTo(product.getName()),
                () -> assertThat(itemDto.available()).isEqualTo(available),
                () -> assertThat(itemDto.quantity()).isEqualTo(quantity)
        );
    }

}