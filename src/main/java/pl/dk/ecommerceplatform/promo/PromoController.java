package pl.dk.ecommerceplatform.promo;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.promo.dtos.PromoDto;
import pl.dk.ecommerceplatform.promo.dtos.SavePromoDto;

import java.net.URI;

@RestController
@RequestMapping("/promos")
@AllArgsConstructor
@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
class PromoController {

    private PromoService promoService;

    @PostMapping("")
    public ResponseEntity<PromoDto> createPromoCode(@Valid @RequestBody SavePromoDto savePromoDto) {
        PromoDto dto = promoService.createPromo(savePromoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoDto> getPromoCode(@PathVariable Long id) {
        PromoDto promoDto = promoService.getPromo(id);
        return ResponseEntity.ok(promoDto);
    }
}
