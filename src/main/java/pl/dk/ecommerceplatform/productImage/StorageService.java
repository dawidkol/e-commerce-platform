package pl.dk.ecommerceplatform.productImage;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.productImage.ImageAlreadyExistsException;
import pl.dk.ecommerceplatform.error.exceptions.productImage.ImageFilePatchNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.productImage.ImageNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.productImage.MultipartFilenameException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.productImage.dtos.ImageDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class StorageService {

    private final ImageFileDataRepository imageFileDataRepository;
    private final ProductRepository productRepository;
    Logger logger = UtilsService.getLogger(this.getClass());
    @Value("${app.storage.location.images}")
    private String storageFolder;

    @Transactional
    public List<ImageDto> uploadImage(Long productId, MultipartFile[] file) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        List<ImageDto> imageList = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            String fileName = file[i].getOriginalFilename();
            if (fileName == null) {
                throw new MultipartFilenameException();
            }
            logger.debug("Starting uploading file...");
            String pathFolder = storageFolder.concat("/%d").formatted(productId);
            File folder = new File(pathFolder);
            if (!folder.exists()) {
                folder.mkdirs();
                logger.debug("Created new folder for product id: {}", productId);
            }

            ImageFileData fileData = ImageFileData
                    .builder()
                    .name(file[i].getOriginalFilename())
                    .type(file[i].getContentType())
                    .filePatch(pathFolder.concat("/%s".formatted(file[i].getOriginalFilename())))
                    .product(product)
                    .build();

            Path filePatch = Path.of(pathFolder).resolve(fileName).normalize();
            try {
                Files.copy(file[i].getInputStream(), filePatch);
            } catch (FileAlreadyExistsException ex) {
                throw new ImageAlreadyExistsException();
            }

            ImageFileData savedImage = imageFileDataRepository.save(fileData);
            ImageDto imageDto = ImageDtoMapper.map(savedImage);
            logger.debug("File upload successfully:" + filePatch);
            imageList.add(imageDto);
        }
        return imageList;
    }

    public byte[] downloadImage(Long id) {
        ImageFileData imageFileData = imageFileDataRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image with id; %d not found".formatted(id)));
        Path path = new File(imageFileData.getFilePatch()).toPath();
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ServerException();
        }
        return bytes;
    }

    @Transactional
    public void deleteAllByIds(List<Long> imagesIds) {
        List<ImageFileData> imageList = imagesIds.stream()
                .map(id -> imageFileDataRepository.findById(id)
                        .orElseThrow(() -> new ImageNotFoundException("Image with id = %d not exists".formatted(id)))
                )
                .toList();
        imageFileDataRepository.deleteAll(imageList);

        String deleteAndCreateMessage = imageList.stream()
                .map(image -> {
                            try {
                                Files.delete(Path.of(image.getFilePatch()));
                            } catch (IOException e) {
                                throw new ImageFilePatchNotFoundException("FilePatch: %s not found".formatted(image.getFilePatch()));
                            }
                            return String.valueOf(image.getId());
                        }
                )
                .reduce("Deleted images[ids]: ", (first, next) -> first + next + " ");
        logger.warn(deleteAndCreateMessage);
    }

}
