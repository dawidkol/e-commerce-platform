package pl.dk.ecommerceplatform.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;


class ShippingControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void saveUpdateAndDeleteShippingMethod() throws Exception {
        // 1. Admin wants to save new shipping method
        String shippingToSaveJson = """
                {
                    "name": "TESTNAME",
                    "shippingCost": 10.99
                }
                """;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/shipping")
                        .contentType(MediaType.APPLICATION_JSON).content(shippingToSaveJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());

        // 2. Admin wants to update shipping price
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        ShippingDto shippingDto = objectMapper.readValue(contentAsString, ShippingDto.class);
        String newPriceJson = """
                {
                    "newPrice": 14.00
                }""";

        mockMvc.perform(MockMvcRequestBuilders.put("/shipping/{id}", shippingDto.id())
                        .contentType(MediaType.APPLICATION_JSON).content(newPriceJson))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // 3. Admin wants to delete shipping method
        mockMvc.perform(MockMvcRequestBuilders.delete("/shipping/{id}", shippingDto.id()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void userWantsToRetrieveAllShippingMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shipping"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}