package pl.dk.ecommerceplatform.brand;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;
import pl.dk.ecommerceplatform.brand.dtos.SaveBrandDto;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
class BrandService {

    private final BrandRepository brandRepository;
    private final BrandDtoMapper brandDtoMapper;

    public BrandDto getBrandById(Long id) {
        return brandRepository.findById(id).map(brandDtoMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<BrandDto> getAllBrands(int page, int size) {
        return brandRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(brandDtoMapper::map)
                .toList();
    }

    @Transactional
    public BrandDto saveBrand(SaveBrandDto saveBrandDto) {
        Optional<Brand> brand = brandRepository.findByNameIgnoreCase(saveBrandDto.name());
        if (brand.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Brand already exists");
        } else {
            Brand brandToSave = brandDtoMapper.map(saveBrandDto);
            Brand savedBrand = brandRepository.save(brandToSave);
            return brandDtoMapper.map(savedBrand);
        }
    }
}
