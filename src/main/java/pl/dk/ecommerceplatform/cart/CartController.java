package pl.dk.ecommerceplatform.cart;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.cart.dtos.AddToCartDto;
import pl.dk.ecommerceplatform.cart.dtos.CartDto;

import java.net.URI;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
class CartController {

    private final CartService cartService;

    @PostMapping("")
    public ResponseEntity<CartDto> addProductToCart(@RequestBody AddToCartDto addToCartDto) {
        CartDto cartDto = cartService.addProductToCart(addToCartDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(cartDto.id()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

}
