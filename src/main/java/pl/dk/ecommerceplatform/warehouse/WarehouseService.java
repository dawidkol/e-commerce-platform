package pl.dk.ecommerceplatform.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
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

import java.util.Optional;

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
        Item item = warehouseRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        try {
            Item itemToUpdate = utils.applyPatch(item, jsonMergePatch, Item.class);
            warehouseRepository.save(itemToUpdate);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }

}
