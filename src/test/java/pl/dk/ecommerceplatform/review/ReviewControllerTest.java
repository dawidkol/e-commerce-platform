package pl.dk.ecommerceplatform.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.review.dtos.CreateReviewDto;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "sebastian.kowalski@test.pl", roles = "CUSTOMER")
    void itShouldCreateReviewWithDifferentProductIdsAndHandleConflicts() throws Exception {
        // 1. User wants to create a review but hasn't purchased the product
        CreateReviewDto dtoWithProductId3 = CreateReviewDto.builder()
                .productId(4L)
                .rating(5)
                .comment("Best product on the planet")
                .build();

        String jsonProductId4 = new ObjectMapper().writeValueAsString(dtoWithProductId3);

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProductId4))
                .andExpect(MockMvcResultMatchers.status().isConflict());


        // 2. User wants to create a review
        CreateReviewDto dtoWithProductId5 = CreateReviewDto.builder()
                .productId(5L)
                .rating(5)
                .comment("Best product on the planet")
                .build();

        String jsonProductId5 = new ObjectMapper().writeValueAsString(dtoWithProductId5);
        mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProductId5))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    public void adminShouldHandleReviewRetrieval() throws Exception {
        // 1. Admin wants to get a review that does not exist
        Long notExistingReviewId = 20L;
        mockMvc.perform(MockMvcRequestBuilders
                .get("/reviews/%d".formatted(notExistingReviewId)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // 2. Admin wants to get an existing review
        Long existingReviewId = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reviews/%d".formatted(existingReviewId)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "sebastian.kowalski@test.pl", roles = "CUSTOMER")
    public void userShouldNotRetrieveExistingReviewWithoutAdminRole() throws Exception {
        // 1. User wants to get an existing review, but does not have ADMIN_ROLE
        Long existingReviewId = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reviews/%d".formatted(existingReviewId)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}