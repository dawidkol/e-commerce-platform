package pl.dk.ecommerceplatform.brand;

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
import pl.dk.ecommerceplatform.brand.dtos.SaveBrandDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BrandControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void itShouldPerformBrandOperations() throws Exception {
        // 1. User wants to retrieve a brand by given id = 1;
        Long brandId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/brands/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 2. User wants to retrieve all brands
        mockMvc.perform(MockMvcRequestBuilders.get("/brands"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 3. Admin wants to save a new brand
        SaveBrandDto brandToSave = SaveBrandDto.builder()
                .name("testBrandName")
                .build();
        String brandToSaveJson = objectMapper.writeValueAsString(brandToSave);

        mockMvc.perform(MockMvcRequestBuilders.post("/brands").content(brandToSaveJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}