package pl.dk.ecommerceplatform.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;


class EmailControllerTest extends BaseIntegrationTest {

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
        mockMvc.perform(MockMvcRequestBuilders.post("/email/contact").contentType(MediaType.APPLICATION_JSON).content(createEmailDtoWithInvalidDataJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 2. User wants to send email with valid data
        String createEmailDtoWithValidDataJson = """
                {
                    "sender": "john.doe@test.pl",
                    "subject": "test subject",
                    "message": "test message"
                }
                """.trim();
        mockMvc.perform(MockMvcRequestBuilders.post("/email/contact").contentType(MediaType.APPLICATION_JSON).content(createEmailDtoWithValidDataJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}