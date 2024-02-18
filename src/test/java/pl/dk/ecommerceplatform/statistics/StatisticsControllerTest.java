package pl.dk.ecommerceplatform.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.signature.qual.FieldDescriptorWithoutPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.statistics.dtos.AvgOrderDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.AssertionsKt.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class StatisticsControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void itShouldReturnTop3SoldProducts() throws Exception {
        // 1. Perform GET request to /stats endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/stats"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(3))))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void itShouldGetAvgStatisticsForAdmin() throws Exception {
        // 1. Perform GET request to /stats/avg endpoint
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/stats/avg"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        AvgOrderDto avgOrderDto = objectMapper.readValue(contentAsString, AvgOrderDto.class);

        assertThat(avgOrderDto).isNotNull();
    }
}