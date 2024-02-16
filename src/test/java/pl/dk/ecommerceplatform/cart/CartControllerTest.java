package pl.dk.ecommerceplatform.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.cart.dtos.AddToCartDto;

class CartControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void itShouldAddProductToCartAndUpdateQuantityThenDeleteAllProductsInCart() throws Exception {
        // 1. User wants to add product to cart
        Long productId = 3L;
        Long productQuantity = 1L;

        AddToCartDto dto = AddToCartDto.builder()
                .productId(productId)
                .quantity(productQuantity)
                .build();

        String stringJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/carts").contentType(MediaType.APPLICATION_JSON).content(stringJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // 2. User wants to update product quantity in cart
        Long newQuantity = 5L;

        AddToCartDto updateQuantityDto = AddToCartDto.builder()
                .productId(productId)
                .quantity(newQuantity)
                .build();

        String updateQuantityJson = objectMapper.writeValueAsString(updateQuantityDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/carts").contentType(MediaType.APPLICATION_JSON).content(updateQuantityJson))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // 3. User wants to delete all products in cart
        mockMvc.perform(MockMvcRequestBuilders.delete("/carts"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}