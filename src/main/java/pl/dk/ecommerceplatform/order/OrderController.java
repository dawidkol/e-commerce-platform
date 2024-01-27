package pl.dk.ecommerceplatform.order;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;
import pl.dk.ecommerceplatform.security.SecurityService;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.net.URI;
import java.util.List;

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

    @PutMapping("")
    public ResponseEntity<?> updateOrderStatus(@Valid @RequestBody UpdateOrderStatusDto orderStatusDto) {
        orderService.updateOrderStatus(orderStatusDto);
        return ResponseEntity.noContent().build();
    }

}
