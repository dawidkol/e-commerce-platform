package pl.dk.ecommerceplatform.warehouse;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.constant.PaginationConstant;
import pl.dk.ecommerceplatform.warehouse.dtos.ItemDto;
import pl.dk.ecommerceplatform.warehouse.dtos.SaveItemDto;

import java.net.URI;
import java.util.List;

import static pl.dk.ecommerceplatform.constant.PaginationConstant.*;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping("")
    public ResponseEntity<ItemDto> saveItem(@RequestBody SaveItemDto saveItemDto) {
        ItemDto item = warehouseService.saveItem(saveItemDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.id())
                .toUri();
        return ResponseEntity.created(uri).body(item);
    }

    @PatchMapping("")
    public ResponseEntity<?> updateItem(@RequestParam Long id, @Valid @RequestBody JsonMergePatch patch) {
        warehouseService.updateItem(id, patch);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<ItemDto>> getItems(@RequestParam(required = false, defaultValue = PAGE_DEFAULT) int page,
                                                  @RequestParam(required = false, defaultValue = SIZE_DEFAULT) int size) {
        List<ItemDto> items = warehouseService.getItems(page, size);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        warehouseService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
