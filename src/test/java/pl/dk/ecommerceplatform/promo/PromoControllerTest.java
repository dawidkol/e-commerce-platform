package pl.dk.ecommerceplatform.promo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.promo.dtos.PromoDto;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

class PromoControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void testPromoCodeEndpoints() throws Exception {
        // 1. Admin wants to create promo code
        LocalDateTime start = LocalDateTime.now().plusHours(1L);
        LocalDateTime end = start.plusDays(2L);

        SavePromoDto savePromoDto = SavePromoDto.builder()
                .code("testcode")
                .discountPercent(5L)
                .activeStart(start)
                .activeEnd(end)
                .active(true)
                .maxUsageCount(10L)
                .build();

        String savePromoDtoString = objectMapper.writeValueAsString(savePromoDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/promos").contentType(MediaType.APPLICATION_JSON).content(savePromoDtoString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        PromoDto promoDto = objectMapper.readValue(contentAsString, PromoDto.class);
        String locationHeader = result.getResponse().getHeader("Location");
        assertThat(locationHeader).contains("http://localhost/promos/%d".formatted(promoDto.id()));

        // 2. Admin wants to retrieve created promo code
        mockMvc.perform(MockMvcRequestBuilders.get("/promos/{id}", promoDto.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // 3. Admin wants to retrieve promo codes
        mockMvc.perform(MockMvcRequestBuilders.get("/promos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(10)));
    }
}