package pl.dk.ecommerceplatform.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;


@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "sebastian.kowalski@test.pl", roles = "CUSTOMER")
    void itShouldPerformOrderRelatedActions() throws Exception {
        // 1. User creates an order
        Long shippingId = 1L;
        Long addressId = 1L;

        SaveOrderDto saveOrderDto = SaveOrderDto.builder()
                .shippingId(shippingId)
                .addressId(addressId)
                .build();

        String orderDtoJson = objectMapper.writeValueAsString(saveOrderDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders").content(orderDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 2. User retrieves his offer by given orderId = 3
        Long orderId = 3L;
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 3. User retrieves all his offers
        mockMvc.perform(MockMvcRequestBuilders.get("/orders", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void itShouldUpdateOrderStatusSuccessfully() throws Exception {
        // 1. Admin wants to update OrderStatus
        UpdateOrderStatusDto orderStatusDto = UpdateOrderStatusDto.builder()
                .orderId(2L)
                .status("paid")
                .build();

        String orderStatusJson = objectMapper.writeValueAsString(orderStatusDto);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/orders").content(orderStatusJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}