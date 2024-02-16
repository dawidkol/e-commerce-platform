package pl.dk.ecommerceplatform.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.category.dtos.SaveCategoryDto;


@AutoConfigureMockMvc
class CategoryControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRetrievingProductsWithValidAndInvalidData() throws Exception {
        // 1. User wants to retrieve products from Electronics category
        mockMvc.perform(MockMvcRequestBuilders.get("/category").param("name", "electronics"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 2. User want to retrieve all products from category that does not exist
        mockMvc.perform(MockMvcRequestBuilders.get("/category").param("name", "not existing category"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void itShouldSaveNewCategory() throws Exception {
        // 1. Admin wants to save new category
        SaveCategoryDto category = SaveCategoryDto.builder()
                .name("new category")
                .build();

        String categoryJson = objectMapper.writeValueAsString(category);

        mockMvc.perform(MockMvcRequestBuilders.post("/category").content(categoryJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 2. Admin wants to save new category but category already exists
        SaveCategoryDto invalidCategory = SaveCategoryDto.builder()
                .name("Electronics")
                .build();

        String invalidCategoryJson = objectMapper.writeValueAsString(category);

        mockMvc.perform(MockMvcRequestBuilders.post("/category").content(invalidCategoryJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }
}