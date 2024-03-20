package pl.dk.ecommerceplatform.productImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.productImage.dtos.ImageDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImageDtoMapperTest {

    @Test
    void itShouldMapToImageDto() {
        // Given
        ImageFileData imageFileData = ImageFileData.builder()
                .id(1L)
                .name("testImageName.jpeg")
                .product(Product.builder().id(1L).build())
                .type(MediaType.IMAGE_JPEG_VALUE)
                .filePatch("./uploads/product_images/1/IMG_7593.jpeg")
                .build();

        // When
        ImageDto imageDto = ImageDtoMapper.map(imageFileData);

        // Then
        assertAll(
                () -> assertThat(imageDto.id()).isEqualTo(imageFileData.getId()),
                () -> assertThat(imageDto.name()).isEqualTo(imageFileData.getName()),
                () -> assertThat(imageDto.type()).isEqualTo(imageFileData.getType()),
                () -> assertThat(imageDto.filePatch()).isEqualTo(imageFileData.getFilePatch()),
                () -> assertThat(imageDto.productId()).isEqualTo(imageFileData.getProduct().getId())
        );
    }
}