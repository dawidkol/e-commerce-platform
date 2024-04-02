package pl.dk.ecommerceplatform.shipping;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/shipping")
@AllArgsConstructor
class ShippingController {

    private final ShippingService shippingService;

    @PutMapping()
    public ResponseEntity<?> updateShippingCost(Long id, BigDecimal newPrice) {
        shippingService.updateShippingCost(id, newPrice);
        return ResponseEntity.noContent().build();
    }
}
