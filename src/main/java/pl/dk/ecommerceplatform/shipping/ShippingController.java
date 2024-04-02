package pl.dk.ecommerceplatform.shipping;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.shipping.dtos.ShippingDto;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/shipping")
@AllArgsConstructor
class ShippingController {

    private final ShippingService shippingService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShippingCost(@PathVariable Long id, @RequestParam BigDecimal newPrice) {
        shippingService.updateShippingCost(id, newPrice);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("")
    public ResponseEntity<ShippingDto> saveShippingMethod(@Valid @RequestBody ShippingDto shippingDto) {
        ShippingDto dto = shippingService.saveShippingMethod(shippingDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
