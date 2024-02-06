package pl.dk.ecommerceplatform.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.brand.Brand;
import pl.dk.ecommerceplatform.category.Category;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Jackson2ObjectMapperBuilder jackson;

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

        mockMvc.perform(MockMvcRequestBuilders.post("/products").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

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

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products").contentType(MediaType.APPLICATION_JSON).content(validJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        ProductDto productDto = objectMapper.readValue(contentAsString, ProductDto.class);
        String locationHeader = resultActions.andReturn().getResponse().getHeader("Location");

        assertThat(locationHeader).contains("http://localhost/products/%d".formatted(productDto.id()));
    }
}