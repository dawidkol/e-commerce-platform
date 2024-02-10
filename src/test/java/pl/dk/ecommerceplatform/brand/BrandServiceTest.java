package pl.dk.ecommerceplatform.brand;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;
import pl.dk.ecommerceplatform.brand.dtos.SaveBrandDto;
import pl.dk.ecommerceplatform.error.exceptions.brand.BrandExistsException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;
    @Mock
    private BrandDtoMapper brandDtoMapper;
    private BrandService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new BrandService(brandRepository, brandDtoMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldRetrieveBrandById() {
        // Given
        Long id = 1L;

        Brand brandMock = mock(Brand.class);
        BrandDto brandDtoMock = mock(BrandDto.class);
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brandMock));
        when(brandDtoMapper.map(brandMock)).thenReturn(brandDtoMock);

        // When
        underTest.getBrandById(id);

        // Then
        verify(brandRepository, times(1)).findById(id);
        verify(brandDtoMapper, times(1)).map(brandMock);
    }

    @Test
    void itShouldRetrieveAllBrandsWithPagination() {
        // Given
        int page = 0;
        int size = 5;

        Brand brandMock = mock(Brand.class);
        BrandDto brandDtoMock = mock(BrandDto.class);
        List<Brand> brandList = List.of(brandMock);

        when(brandRepository.findAll(PageRequest.of(page, size))).thenReturn(new PageImpl<>(brandList));
        when(brandDtoMapper.map(brandMock)).thenReturn(brandDtoMock);
        // When
        underTest.getAllBrands(page, size);

        // Then
        verify(brandRepository, times(1)).findAll(PageRequest.of(page, size));
        verify(brandDtoMapper, times(1)).map(brandMock);
    }

    @Test
    void itShouldSaveNewBrand() {
        // Given
        SaveBrandDto brandToSave = SaveBrandDto.builder()
                .name("testBrandName")
                .build();

        Brand brandMock = mock(Brand.class);
        BrandDto brandDtoMock = mock(BrandDto.class);

        when(brandRepository.findByNameIgnoreCase(brandToSave.name())).thenReturn(Optional.empty());
        when(brandDtoMapper.map(brandToSave)).thenReturn(brandMock);
        when(brandRepository.save(brandMock)).thenReturn(brandMock);
        when(brandDtoMapper.map(brandMock)).thenReturn(brandDtoMock);

        // When
        underTest.saveBrand(brandToSave);

        // Then
        verify(brandRepository, times(1)).findByNameIgnoreCase(brandToSave.name());
        verify(brandDtoMapper, times(1)).map(brandToSave);
        verify(brandRepository, times(1)).save(brandMock);
        verify(brandDtoMapper, times(1)).map(brandMock);
    }

    @Test
    void itShouldThrowBrandExistsException() {
        // Given
        SaveBrandDto brandToSave = SaveBrandDto.builder()
                .name("testBrandName")
                .build();
        Brand brandMock = mock(Brand.class);

        when(brandRepository.findByNameIgnoreCase(brandToSave.name())).thenReturn(Optional.of(brandMock));

        // When
        // Then
        assertThrows(BrandExistsException.class, () -> underTest.saveBrand(brandToSave));
        verify(brandRepository, times(1)).findByNameIgnoreCase(brandToSave.name());
    }
}