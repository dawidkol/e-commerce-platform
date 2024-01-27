package pl.dk.ecommerceplatform.order;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.security.SecurityService;

import java.net.URI;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;

    @PostMapping("")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody SaveOrderDto saveOrderDto) {
        Long idFromSecurityContext = securityService.getIdFromSecurityContextOrThrowException();
        OrderDto orderDto = orderService.createOrder(idFromSecurityContext, saveOrderDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(orderDto);
    }
}
