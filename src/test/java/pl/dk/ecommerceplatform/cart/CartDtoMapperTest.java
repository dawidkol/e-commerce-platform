package pl.dk.ecommerceplatform.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.cart.dtos.CartDto;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRole;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CartDtoMapperTest {

    private CartDtoMapper underTest;

    @BeforeEach
    void init() {
        CartProductsDAO mockCartProductDao = mock(CartProductsDAO.class);
        underTest = new CartDtoMapper(mockCartProductDao);
    }

    @Test
    void itShouldMapToCartDto() {
        // Given
        String email = "john.doe@example.com";
        String password = "testPassword";
        String fistName = "John";
        String lastName = "Doe";
        Long id = 1L;

        UserRole customerRole = UserRole.builder()
                .id(id)
                .name("CUSTOMER")
                .description("description").build();

        User user = User.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .userRole(customerRole)
                .build();

        Product product1 = Product.builder()
                .id(1L)
                .price(new BigDecimal("99.99"))
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .price(new BigDecimal("0.01"))
                .build();

        Cart cart = Cart.builder()
                .id(id)
                .products(List.of(product1, product2))
                .user(user)
                .used(false)
                .build();

        // When
        CartDto cartDto = underTest.map(cart);

        // Then
        assertAll(
                () -> assertThat(cartDto.id()).isEqualTo(id),
                () -> assertThat(cartDto.userId()).isEqualTo(id),
                () -> assertThat(cartDto.products()).hasSize(2),
                () -> assertThat(cartDto.currentCartValue()).isEqualTo(new BigDecimal("100.00"))
        );
    }

        @Test
        void itShouldMapToCartDtoWhenProductHasPromotionPrice() {
            // Given
            String email = "john.doe@example.com";
            String password = "testPassword";
            String fistName = "John";
            String lastName = "Doe";
            Long id = 1L;

            UserRole customerRole = UserRole.builder()
                    .id(id)
                    .name("CUSTOMER")
                    .description("description").build();

            User user = User.builder()
                    .id(id)
                    .firstName(fistName)
                    .lastName(lastName)
                    .email(email)
                    .password(password)
                    .userRole(customerRole)
                    .build();

            Product product1 = Product.builder()
                    .id(1L)
                    .price(new BigDecimal("99.99"))
                    .promotionPrice(new BigDecimal("89.99"))
                    .build();

            Product product2 = Product.builder()
                    .id(2L)
                    .price(new BigDecimal("0.01"))
                    .build();

            Cart cart = Cart.builder()
                    .id(id)
                    .products(List.of(product1, product2))
                    .user(user)
                    .used(false)
                    .build();

            // When
            CartDto cartDto = underTest.map(cart);

            // Then
            assertAll(
                    () -> assertThat(cartDto.id()).isEqualTo(id),
                    () -> assertThat(cartDto.userId()).isEqualTo(id),
                    () -> assertThat(cartDto.products()).hasSize(2),
                    () -> assertThat(cartDto.currentCartValue()).isEqualTo(new BigDecimal("90.00"))
            );
    }

}