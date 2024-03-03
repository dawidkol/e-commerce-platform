package pl.dk.ecommerceplatform.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.io.PrintWriter;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dk.ecommerceplatform.user.Role.*;


class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Logger logger;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        logger = UtilsService.getLogger(this.getClass());
    }

    @Test
    void testUserRegistrationScenarios() throws Exception {
        // 1. User tries to register with invalid data
        String invalidEmail = "invalidEmail";
        String password = "testPassword";
        String fistName = "John";
        String lastName = "Doe";
        RegisterUserDto invalidJsonString = RegisterUserDto.builder()
                .firstName(fistName)
                .lastName(lastName)
                .email(invalidEmail)
                .password(password)
                .build();

        String invalidDto = objectMapper.writeValueAsString(invalidJsonString);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDto))
                .andExpect(status().isBadRequest());

        // 2. User tries to register with an existing email
        String existingEmail = "janusz.kowalski@test.pl";
        RegisterUserDto validDtoWithExistingEmail = RegisterUserDto.builder()
                .firstName(fistName)
                .lastName(lastName)
                .email(existingEmail)
                .password(password)
                .build();

        String validJsonStringWithExistingEmail = objectMapper.writeValueAsString(validDtoWithExistingEmail);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonStringWithExistingEmail))
                .andExpect(status().isConflict());

        // 3. User successfully registers with valid data
        String validEmail = "john.doe@example.com";
        RegisterUserDto validDto = RegisterUserDto.builder()
                .firstName(fistName)
                .lastName(lastName)
                .email(validEmail)
                .password(password)
                .build();

        String validJsonString = objectMapper.writeValueAsString(validDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonString))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "sebastian.kowalski@test.pl", roles = "CUSTOMER")
    void testCustomerUpdateEmail() throws Exception {
        // Given
        String email = "newemail@test.pl";
        String stringJson = """
                {
                "email": "%s"
                }
                """
                .formatted(email)
                        .trim();

        // When
        // Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void adminRegistersNewEmployeeAndDeletes() throws Exception {
        // 1. Admin wants to register new employee
        String employeeJson = """
                {
                    "firstName": "Jan",
                    "lastName": "Kot",
                    "email": "customer@email.com",
                    "password": "123abcd"
                }
                """;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/employee").content(employeeJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", is(EMPLOYEE.name())))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(contentAsString, UserDto.class);

        // 2. Admin wants to delete employee by given email
        String email = userDto.email();
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{email}", email))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}