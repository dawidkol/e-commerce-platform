package pl.dk.ecommerceplatform.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.cart.dtos.AddToCartDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.io.IOException;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
class BearerTokenFilterTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    Logger logger = UtilsService.getLogger(this.getClass());

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void itShouldAddProductToCartAfterAuthentication() throws Exception {
        // Given
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

        String stringResult = result.getResponse().getContentAsString();
        Wrapper wrapper = objectMapper.readValue(stringResult, Wrapper.class);
        logger.warn(wrapper.token);

        Long productId = 3L;
        Long productQuantity = 1L;

        AddToCartDto dto = AddToCartDto.builder()
                .productId(productId)
                .quantity(productQuantity)
                .build();

        String stringJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("Authorization", "Bearer " + wrapper.token)
                        .content(stringJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void itShouldThrowParseException() throws ServletException, IOException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String sharedKeyForTests = "8e55772b-26c5-4114-bbe9-cb6d44af2ce4";
        BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(new JwtService(sharedKeyForTests));
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");

        // When
        bearerTokenFilter.doFilter(request, response, filterChain);
    }

    private record Wrapper(String username, String token) {
    }
}