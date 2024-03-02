package pl.dk.ecommerceplatform.address;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;
import pl.dk.ecommerceplatform.address.dtos.SaveAddressDto;

import java.net.URI;

@RestController
@RequestMapping("/address")
@AllArgsConstructor
class AddressController {

    private AddressService addressService;

    @PostMapping("")
    @PreAuthorize(value = "hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<AddressDto> createShippingAddress(@Valid @RequestBody SaveAddressDto saveAddressDto) {
        AddressDto shippingAddress = addressService.createShippingAddress(saveAddressDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(shippingAddress.id())
                .toUri();
        return ResponseEntity.created(uri).body(shippingAddress);
    }

    @PutMapping("")
    @PreAuthorize(value = "hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateShippingAddress(@Valid @RequestBody SaveAddressDto saveAddressDto) {
        addressService.updateShippingAddress(saveAddressDto);
        return ResponseEntity.noContent().build();
    }
}
