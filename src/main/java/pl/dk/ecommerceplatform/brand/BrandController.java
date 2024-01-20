package pl.dk.ecommerceplatform.brand;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dk.ecommerceplatform.brand.dtos.BrandDto;

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

}
