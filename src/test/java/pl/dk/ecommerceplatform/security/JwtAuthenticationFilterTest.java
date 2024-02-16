package pl.dk.ecommerceplatform.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
class JwtAuthenticationFilterTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAuthenticationEndpoint() throws Exception {
        // 1. User wants to get token but he's not registered
        String nonRegisteredUser = """
                {
                    "username": "tomasz.kowalski@test.pl",
                    "password": "password"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth").content(nonRegisteredUser).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // 2. Registered user wants to get token
        String user = """
                {
                    "username": "sebastian.kowalski@test.pl",
                    "password": "password"
                }
                """;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("token"));
    }
}