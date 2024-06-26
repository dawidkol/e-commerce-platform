package pl.dk.ecommerceplatform.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;


class ContactControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenInvalidEmailDataWhenSendingEmailThenReturnBadRequestAndValidDataReturnsOk() throws Exception {
        // 1. User wants to send email with invalid data
        String createEmailDtoWithInvalidDataJson = """
                {
                    "sender": "john.doetest.pl",
                    "subject": "test subject",
                    "message": "test message"
                }
                """.trim();
        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createEmailDtoWithInvalidDataJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 2. User wants to send email with valid data
        String createEmailDtoWithValidDataJson = """
                {
                    "sender": "john.doe@test.pl",
                    "subject": "test subject",
                    "message": "test message"
                }
                """.trim();
        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createEmailDtoWithValidDataJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void itShouldSendResponseMessage() throws Exception {
        String contactResponseDtoJson = """
                {
                    "contactId": 1,
                    "response": "This is example response message."
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/contact/response")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactResponseDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}