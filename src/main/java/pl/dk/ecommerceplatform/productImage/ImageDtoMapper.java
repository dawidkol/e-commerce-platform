package pl.dk.ecommerceplatform.productImage;

import pl.dk.ecommerceplatform.productImage.dtos.ImageDto;

class ImageDtoMapper {

    public static ImageDto map(ImageFileData imageFileData) {
        return ImageDto.builder()
                .id(imageFileData.getId())
                .name(imageFileData.getName())
                .type(imageFileData.getType())
                .filePatch(imageFileData.getFilePatch())
                .productId(imageFileData.getProduct().getId())
                .build();
    }
}
