package pl.dk.ecommerceplatform.brand;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;
import pl.dk.ecommerceplatform.brand.dtos.SaveBrandDto;

@Service
class BrandDtoMapper {

    public BrandDto map(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }

    public Brand map(SaveBrandDto saveBrandDto) {
        return Brand.builder()
                .name(saveBrandDto.name())
                .build();
    }
}
