package pl.dk.ecommerceplatform.productImage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.ecommerceplatform.BaseIntegrationTest;

class ImageControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "janusz.kowalski@test.pl", roles = "ADMIN")
    void testImageManagementOperationsWithAdminRole() throws Exception {
        // 1.  Admin wants to upload file with invalid extension
        Long productId = 1L;
        MockMultipartFile invalidFileExtension = new MockMultipartFile("image",
                "test-image1.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "image1".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/images/{productId}", productId)
                        .file(invalidFileExtension)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 2. Admin wants to upload files with valid files extension
        MockMultipartFile image1 = new MockMultipartFile("image",
                "test-image1.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "image1".getBytes());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/images/{productId}", productId)
                        .file(image1)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String[] segments = resultActions.andReturn().getResponse().getHeader("Location").split("/");
        String imageId = segments[segments.length - 1];

        // 3. Admin wants to download image
         mockMvc.perform(MockMvcRequestBuilders.get("/images/{id}", imageId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG));

        // 4. Admin wants to delete image with id = 1
        mockMvc.perform(MockMvcRequestBuilders.delete("/images/{id}", imageId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}