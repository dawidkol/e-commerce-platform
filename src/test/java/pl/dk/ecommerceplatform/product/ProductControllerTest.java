package pl.dk.ecommerceplatform.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.DESC;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void testSavingProductWithValidAndInvalidData() throws Exception {
        // 1.  Admin wants to save product with invalid data
        String invalidName = "AA";
        String description = "Test description";
        BigDecimal price = new BigDecimal("99.99");

        Long categoryId = 1L;
        Long brandId = 1L;

        SaveProductDto invalidProductDto = SaveProductDto.builder()
                .name(invalidName)
                .description(description)
                .price(price)
                .categoryId(categoryId)
                .brandId(brandId)
                .build();

        String invalidJson = objectMapper.writeValueAsString(invalidProductDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/products").contentType(APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest());

        // 2.  Admin wants to save product with valid data
        String validName = "Test product";

        SaveProductDto validProductDto = SaveProductDto.builder()
                .name(validName)
                .description(description)
                .price(price)
                .categoryId(categoryId)
                .brandId(brandId)
                .build();

        String validJson = objectMapper.writeValueAsString(validProductDto);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products").contentType(APPLICATION_JSON).content(validJson))
                .andExpect(status().isCreated());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        ProductDto productDto = objectMapper.readValue(contentAsString, ProductDto.class);
        String locationHeader = resultActions.andReturn().getResponse().getHeader("Location");

        assertThat(locationHeader).contains("http://localhost/products/%d".formatted(productDto.id()));
    }

    @Test
    void testGetProductByIdEndpoint() throws Exception {
        // 1. User wants to get product by given id = 1
        Long existingId = 1L;

        ResultActions resultActionsOk = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId))
                .andExpect(status().isOk());

        String contentAsString = resultActionsOk.andReturn().getResponse().getContentAsString();
        assertThat(contentAsString).isNotEmpty();

        // 2. User wants to get product that not exists by given id = 99;
        Long notExistingId = 99L;
        ResultActions resultActionsNoContent = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", notExistingId))
                .andExpect(status().isNoContent());

        String emptyContentString = resultActionsNoContent.andReturn().getResponse().getContentAsString();
        assertThat(emptyContentString).isEmpty();
    }

    @Test
    void testGetProducts_DefaultAndCustomParameters() throws Exception {
        // 1. Test getProducts by default parameters
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(10))));

        // 2. Test getProducts by given parameters
        int page = 1;
        int size = 7;
        String property = "id";
        String dir = DESC;

        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("property", property)
                        .param("dir", dir))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(size))));
    }

    @Test
    void testGetProductsByNameAndCategory() throws Exception {
        // 1. Test getProductsByNameAndCategory with a given name
        String name = "apple";
        String category = "Electronics";
        mockMvc.perform(MockMvcRequestBuilders.get("/products/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThan(1))));

        // 2. Test getProductsByNameAndCategory with a given a name and category
        mockMvc.perform(MockMvcRequestBuilders.get("/products/search")
                        .param("name", name)
                        .param("category", category))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))));

        // 3. Test getProductsByNameAndCategory with a non-existing product name and existing category
        String notExistingProductName = "NotExistingProductName";
        mockMvc.perform(MockMvcRequestBuilders.get("/products/search")
                        .param("name", notExistingProductName)
                        .param("category", category))
                .andExpect(status().isNoContent());
    }

    @Test
    void itShouldRetrieveReviewsByProductId() throws Exception {
        // 1. User wants to get reviews by given product id = 1
        Long productId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}/reviews", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.averageProductReview").isNumber());

        // 2. User wants to get reviews that not exists by given product id = 99
        Long notExistingId = 99L;
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}/reviews", notExistingId))
                .andExpect(status().isNoContent());
    }
}