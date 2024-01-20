package pl.dk.ecommerceplatform.brand;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;

@Service
class BrandDtoMapper {

    public BrandDto map(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
