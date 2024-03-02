package pl.dk.ecommerceplatform.address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class AddressControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "sebastian.kowalski@test.pl", roles = "CUSTOMER")
    void itShouldCreateAndThenUpdateShippingAddress() throws Exception {
        // 1. User wants to create shipping address
        String createAddressJson = """
                {
                    "postalCode": "22-400",
                    "street": "testow ulica",
                    "buildingNumber": "20",
                    "phoneNumber": "666666666"
                }
                """;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/address").content(createAddressJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", notNullValue()));

        String header = resultActions.andReturn().getResponse().getHeader("Location");
        List<String> pathSegments = ServletUriComponentsBuilder.fromUriString(Objects.requireNonNull(header)).build().getPathSegments();
        Long id = Long.valueOf(pathSegments.get(pathSegments.size() -1));

        // 2. User wants to update shipping address
        String newAddressJson = """
                {
                    "id": %d,
                    "postalCode": "22-400",
                    "street": "testowa ulica",
                    "buildingNumber": "20",
                    "phoneNumber": "666666666"
                }
                """.formatted(id);
        mockMvc.perform(MockMvcRequestBuilders.put("/address").content(newAddressJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}