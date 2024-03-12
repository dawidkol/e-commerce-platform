package pl.dk.ecommerceplatform.warehouse;

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
import pl.dk.ecommerceplatform.warehouse.dtos.SaveItemDto;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.SIZE_DEFAULT;

class WarehouseControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void testWarehouseControllerFunctionality() throws Exception {
        // 1. Test saving an item with invalid product ID
        Long existingProductId = 1L;
        Long itemQuantity = 10L;
        boolean available = true;

        SaveItemDto saveItemDtoWithInvalidProductId = SaveItemDto.builder()
                .productId(existingProductId)
                .quantity(itemQuantity)
                .available(available)
                .build();

        String invalidJsonString = objectMapper.writeValueAsString(saveItemDtoWithInvalidProductId);

        mockMvc.perform(MockMvcRequestBuilders.post("/items").content(invalidJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        // 2. Test saving an item with valid product ID
        Long validProductId = 28L;

        SaveItemDto saveItemDtoWithValidProductId = SaveItemDto.builder()
                .productId(validProductId)
                .quantity(itemQuantity)
                .available(available)
                .build();

        String validJsonString = objectMapper.writeValueAsString(saveItemDtoWithValidProductId);

        mockMvc.perform(MockMvcRequestBuilders.post("/items").content(validJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 3. Test updating the quantity of an existing item
        Long itemId = 1L;
        String content = """
                {
                    "quantity": 99
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{id}", itemId)
                        .content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // 4. Test retrieving all items with default paging and sorting
        mockMvc.perform(MockMvcRequestBuilders.get("/items"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(Integer.parseInt(SIZE_DEFAULT))));

        // 5. Test deleting an item
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}