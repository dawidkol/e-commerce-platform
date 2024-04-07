package pl.dk.ecommerceplatform.order;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.OrderValueDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;
import pl.dk.ecommerceplatform.security.SecurityService;

import java.net.URI;
import java.util.List;

import static pl.dk.ecommerceplatform.constant.PaginationConstant.PAGE_DEFAULT;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.SIZE_DEFAULT;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
class OrderController {

    private final OrderServiceImpl orderService;
    private final SecurityService securityService;

    @PostMapping("")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody SaveOrderDto saveOrderDto) {
        Long idFromSecurityContext = securityService.getIdFromSecurityContextOrThrowException();
        OrderDto orderDto = orderService.createOrder(idFromSecurityContext, saveOrderDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(orderDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusDto orderStatusDto) {
        orderService.updateOrderStatus(id, orderStatusDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestParam(required = false, defaultValue = PAGE_DEFAULT) int page,
                                                       @RequestParam(required = false, defaultValue = SIZE_DEFAULT) int size) {
        SecurityResult result = this.getResult();
        List<OrderDto> orders = orderService.getOrders(result.credentials, result.userId, page, size);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<OrderDto> getOrder(@PathVariable(name = "id") Long orderId) {
        SecurityResult result = this.getResult();
        OrderDto orderDto = orderService.getOrder(result.credentials(), orderId, result.userId());
        return ResponseEntity.ok(orderDto);
    }

    private SecurityResult getResult() {
        List<String> credentials = securityService.getCredentials();
        Long idFromSecurityContext = securityService.getIdFromSecurityContextOrThrowException();
        return new SecurityResult(credentials, idFromSecurityContext);
    }

    private record SecurityResult(List<String> credentials, Long userId) {
    }

    @GetMapping("/{id}/value")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<OrderValueDto> calculateOrderValueWithAnotherCurrency(@PathVariable Long id, @RequestParam String code) {
        OrderValueDto orderValueDto = orderService.calculateOrderValueWithAnotherValue(id, code);
        return ResponseEntity.ok(orderValueDto);
    }
}
