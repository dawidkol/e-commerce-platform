package pl.dk.ecommerceplatform.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.ItemExistsException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.ItemNotFoundException;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.utils.UtilsService;
import pl.dk.ecommerceplatform.warehouse.dtos.ItemDto;
import pl.dk.ecommerceplatform.warehouse.dtos.SaveItemDto;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.*;

@Service
@AllArgsConstructor
class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UtilsService utils;
    private final ItemDtoMapper itemDtoMapper;

    @Transactional
    public ItemDto saveItem(SaveItemDto saveItemDto) {
        Product product = productRepository.findById(saveItemDto.productId()).orElseThrow(ProductNotFoundException::new);
        Optional<Item> productOptional = warehouseRepository.findByProduct_id(product.getId());
        if (productOptional.isPresent()) throw new ItemExistsException();
        Item itemToSave = itemDtoMapper.map(saveItemDto, product);
        Item savedItem = warehouseRepository.save(itemToSave);
        return itemDtoMapper.map(savedItem);
    }

    @Transactional
    public void updateItem(Long id, JsonMergePatch jsonMergePatch) {
        SaveItemDto dataToUpdate = this.prepareDataToUpdate(id);
        try {
            SaveItemDto updateItemDto = utils.applyPatch(dataToUpdate, jsonMergePatch, SaveItemDto.class);
            Product product = productRepository.findById(updateItemDto.productId()).orElseThrow(ProductNotFoundException::new);
            Item itemToUpdate = itemDtoMapper.map(updateItemDto, product);
            warehouseRepository.save(itemToUpdate);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }

    private SaveItemDto prepareDataToUpdate(Long id) {
        Optional<Item> optionalItem = warehouseRepository.findById(id);
        Item item = optionalItem.orElseThrow(ItemNotFoundException::new);
        Product product = item.getProduct();
        return SaveItemDto.builder()
                .id(item.getId())
                .productId(product.getId())
                .quantity(item.getQuantity())
                .available(item.isAvailable())
                .build();
    }

    private record ItemUpdateWrapper(SaveItemDto saveItemDto, Product product) {
    }

    public List<ItemDto> getItems(int page, int size) {
        return warehouseRepository.findAll(PageRequest.of(page, size, ASC, SORT_ID))
                .stream().map(itemDtoMapper::map)
                .toList();
    }

    @Transactional
    public void deleteItem(Long id) {
        warehouseRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        warehouseRepository.deleteById(id);
    }

}
