package pl.dk.ecommerceplatform.brand;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;
import pl.dk.ecommerceplatform.brand.dtos.SaveBrandDto;

import java.net.URI;
import java.util.List;

import static pl.dk.ecommerceplatform.constant.PaginationConstant.PAGE_DEFAULT;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.SIZE_DEFAULT;

@RestController
@RequestMapping("/brands")
@AllArgsConstructor
class BrandController {

    private final BrandService brandService;

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<BrandDto>> getAllBrands(@RequestParam(required = false, defaultValue = PAGE_DEFAULT) int page,
                                                       @RequestParam(required = false, defaultValue = SIZE_DEFAULT) int size) {
        List<BrandDto> brands = brandService.getAllBrands(page, size);
        return ResponseEntity.ok(brands);
    }

    @PostMapping("")
    public ResponseEntity<BrandDto> saveBrand(@Valid @RequestBody SaveBrandDto saveBrandDto) {
        BrandDto brandDto = brandService.saveBrand(saveBrandDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(brandDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(brandDto);
    }

}
