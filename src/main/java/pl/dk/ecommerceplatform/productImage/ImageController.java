package pl.dk.ecommerceplatform.productImage;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.productImage.dtos.ImageDto;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
class ImageController {

    private final StorageService storageService;

    @PostMapping("/{productId}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<ImageDto>> uploadImage(@PathVariable Long productId, @RequestParam("images") MultipartFile[] file) throws IOException {
        List<ImageDto> imageList = storageService.uploadImage(file, productId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(productId)
                .toUri();
        return ResponseEntity.created(uri).body(imageList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> downloadProductImages(@PathVariable Long id) {
        byte[] bytes = storageService.downloadImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }
}
