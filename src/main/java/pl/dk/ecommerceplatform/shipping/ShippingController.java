package pl.dk.ecommerceplatform.shipping;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shipping")
@AllArgsConstructor
class ShippingController {

    private final ShippingService shippingService;

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateShippingCost(@PathVariable Long id, @RequestParam BigDecimal newPrice) {
        shippingService.updateShippingCost(id, newPrice);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<ShippingDto> saveShippingMethod(@Valid @RequestBody ShippingDto shippingDto) {
        ShippingDto dto = shippingService.saveShippingMethod(shippingDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteShippingMethod(@PathVariable Long id) {
        shippingService.deleteShipping(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ShippingDto>> getShippingMethod() {
        List<ShippingDto> shippingMethods = shippingService.getShippingMethods();
        return ResponseEntity.ok(shippingMethods);
    }
}
