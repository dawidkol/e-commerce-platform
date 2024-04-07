package pl.dk.ecommerceplatform.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.currency.CurrencyCode;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;


@AutoConfigureMockMvc
class OrderControllerTest extends BaseIntegrationTest {

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

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/orders").content(orderDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 2. User wants to calculate order value with another currency
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        OrderDto orderDto = objectMapper.readValue(contentAsString, OrderDto.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}/value", orderDto.id())
                        .param("code", CurrencyCode.EUR.name().toUpperCase()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyCode").value(CurrencyCode.EUR.name()));

        // 3. User retrieves his offer by given orderId = 3
        Long orderId = 3L;
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 4. User retrieves all his offers
        mockMvc.perform(MockMvcRequestBuilders.get("/orders", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void itShouldUpdateOrderStatusSuccessfully() throws Exception {
        // 1. Admin wants to update OrderStatus
        Long orderId = 2L;
        UpdateOrderStatusDto orderStatusDto = UpdateOrderStatusDto.builder()
                .status(OrderStatus.PAID.name())
                .build();

        String orderStatusJson = objectMapper.writeValueAsString(orderStatusDto);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", orderId).content(orderStatusJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}