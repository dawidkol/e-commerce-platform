package pl.dk.ecommerceplatform.cart;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.cart.dtos.AddToCartDto;
import pl.dk.ecommerceplatform.cart.dtos.CartDto;
import pl.dk.ecommerceplatform.security.SecurityService;

import java.net.URI;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
class CartController {

    private final CartService cartService;
    private final SecurityService securityService;

    @PostMapping("")
    public ResponseEntity<CartDto> addProductToCart(@Valid @RequestBody AddToCartDto addToCartDto) {
        Long userIdFromSecurityContext = securityService.getIdFromSecurityContextOrThrowException();
        CartDto cartDto = cartService.addProductToCart(userIdFromSecurityContext, addToCartDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(cartDto.id()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @DeleteMapping("")
    public ResponseEntity<?> cleanUserCart() {
        Long userIdFromSecurityContext = securityService.getIdFromSecurityContextOrThrowException();
        cartService.cleanUserCart(userIdFromSecurityContext);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("")
    public ResponseEntity<?> updateProductQuantityInCart(@Valid @RequestBody AddToCartDto addToCartDto) {
        Long userIdFromSecurityContext = securityService.getIdFromSecurityContextOrThrowException();
        cartService.updateProductQuantityInCart(userIdFromSecurityContext, addToCartDto);
        return ResponseEntity.noContent().build() ;
    }
}
