package pl.dk.ecommerceplatform.productImage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.error.exceptions.productImage.ImageFilePatchNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.productImage.ImageNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.product.ProductRepository;

import javax.annotation.meta.When;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StorageServiceTest {

    @Mock
    private ImageFileDataRepository imageFileDataRepository;
    @Mock
    private ProductRepository productRepository;
    private StorageService underTest;
    private AutoCloseable autoCloseable;


    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StorageService(imageFileDataRepository, productRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void itShouldDownloadImage() throws IOException {
        // Given
        Long id = 1L;
        byte[] fileContent = "Mock file content".getBytes();
        ImageFileData imageFileData = ImageFileData.builder().filePatch("/path/to/mockfile.jpg").build();
        when(imageFileDataRepository.findById(id)).thenReturn(Optional.of(imageFileData));
        Path mockFilePath = Path.of(imageFileData.getFilePatch());
        MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
        when(Files.readAllBytes(mockFilePath)).thenReturn(fileContent);

        // When
        byte[] downloadedBytes = underTest.downloadImage(id);

        // Then
        verify(imageFileDataRepository, times(1)).findById(id);
        assertNotNull(downloadedBytes);
        assertArrayEquals(fileContent, downloadedBytes);
        filesMockedStatic.close();
    }

    @Test
    public void itShouldThrowImageNotFoundException() {
        // Given
        Long id = 1L;
        when(imageFileDataRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ImageNotFoundException.class, () -> underTest.downloadImage(id));
        verify(imageFileDataRepository, times(1)).findById(id);
    }

    @Test
    public void itShouldThrowServerException() throws IOException {
        // Given
        Long id = 1L;
        ImageFileData imageFileData = ImageFileData.builder().filePatch("/path/to/nonexistentfile.jpg").build();
        when(imageFileDataRepository.findById(id)).thenReturn(Optional.of(imageFileData));

        Path mockFilePath = Path.of(imageFileData.getFilePatch());
        MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
        when(Files.readAllBytes(mockFilePath)).thenThrow(IOException.class);

        // When
        // Then
        assertThrows(ServerException.class, () -> underTest.downloadImage(id));
        verify(imageFileDataRepository, times(1)).findById(id);
        filesMockedStatic.close();
    }

    @Test
    public void itShouldDeleteAllImagesByGivenIds() throws IOException {
        // Given
        List<Long> imagesIds = List.of(1L, 2L);
        ImageFileData imageFileData1 = ImageFileData.builder().id(1L).filePatch("/path/to/image1.jpg").build();
        ImageFileData imageFileData2 = ImageFileData.builder().id(1L).filePatch("/path/to/image2.jpg").build();
        List<ImageFileData> imageList = List.of(imageFileData1, imageFileData2);

        when(imageFileDataRepository.findById(1L)).thenReturn(Optional.of(imageFileData1));
        when(imageFileDataRepository.findById(2L)).thenReturn(Optional.of(imageFileData2));

        MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
        doNothing().when(Files.class);
        Files.delete(any(Path.class));

        // When
        underTest.deleteAllByIds(imagesIds);

        // Then
        verify(imageFileDataRepository, times(1)).findById(1L);
        verify(imageFileDataRepository, times(1)).findById(2L);
        verify(imageFileDataRepository, times(1)).deleteAll(imageList);
        filesMockedStatic.close();
    }

    @Test
    public void itShouldThrowImageNotFoundExceptionWhenImageIsNotFound() {
        // Given
        List<Long> imagesIds = List.of(1L);
        when(imageFileDataRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ImageNotFoundException.class, () -> underTest.deleteAllByIds(imagesIds));
        verify(imageFileDataRepository, times(1)).findById(1L);
    }

    @Test
    public void itShouldThrowImageFilePatchNotFoundException() throws IOException {
        // Given
        List<Long> imagesIds = List.of(1L);
        ImageFileData imageFileData = ImageFileData.builder().id(1L).filePatch("nonexistentfile.jpg").build();
        when(imageFileDataRepository.findById(1L)).thenReturn(Optional.of(imageFileData));

        MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
        doThrow(new IOException()).when(Files.class);
        Files.delete(any(Path.class));

        // When
        // Then
        assertThrows(ImageFilePatchNotFoundException.class, () -> underTest.deleteAllByIds(imagesIds));
        verify(imageFileDataRepository, times(1)).findById(1L);
        filesMockedStatic.close();
    }
}