package pl.dk.ecommerceplatform.brand;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;

import java.util.List;

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
}
