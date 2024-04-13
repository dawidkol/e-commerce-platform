package pl.dk.ecommerceplatform.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.*;
import static pl.dk.ecommerceplatform.constant.PaginationConstant.SORT_ID;

class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UtilsService utilsService;
    @Mock
    private ItemDtoMapper itemDtoMapper;
    private WarehouseService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new WarehouseService(warehouseRepository, productRepository, utilsService, itemDtoMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSaveItem() {
        // Given
        Long productId = 1L;
        Long quantity = 10L;
        boolean available = true;

        SaveItemDto saveItemDto = SaveItemDto.builder()
                .productId(productId)
                .quantity(quantity)
                .available(available)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByProduct_id(productId)).thenReturn(Optional.empty());
        Item itemToSave = Item.builder().build();
        when(itemDtoMapper.map(saveItemDto, product)).thenReturn(itemToSave);
        when(warehouseRepository.save(itemToSave)).thenReturn(itemToSave);
        ItemDto itemDto = ItemDto.builder().build();
        when(itemDtoMapper.map(itemToSave)).thenReturn(itemDto);

        // When
        underTest.saveItem(saveItemDto);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(warehouseRepository, times(1)).findByProduct_id(productId);
        verify(itemDtoMapper, times(1)).map(saveItemDto, product);
        verify(warehouseRepository, times(1)).save(itemToSave);
        verify(itemDtoMapper, times(1)).map(itemToSave);
    }

    @Test
    void itShouldThrowItemExistException() {
        // Given
        Long productId = 1L;
        Long quantity = 10L;
        boolean available = true;

        SaveItemDto saveItemDto = SaveItemDto.builder()
                .productId(productId)
                .quantity(quantity)
                .available(available)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Item item = Item.builder().build();
        when(warehouseRepository.findByProduct_id(productId)).thenReturn(Optional.of(item));

        // When
        // Then
        assertThrows(ItemExistsException.class, () -> underTest.saveItem(saveItemDto));
        verify(productRepository, times(1)).findById(productId);
        verify(warehouseRepository, times(1)).findByProduct_id(productId);

    }

    @Test
    void itShouldUpdateItem() throws JsonPatchException, JsonProcessingException {
        // Given
        Long productId = 1L;
        Long itemId = 10L;
        Long quantity = 10L;
        boolean available = true;

        Product product = Product.builder()
                .id(productId)
                .build();

        Item item = Item.builder()
                .quantity(quantity)
                .available(available)
                .product(product)
                .build();


        SaveItemDto saveItemDto = SaveItemDto.builder()
                .productId(productId)
                .quantity(quantity)
                .available(available)
                .build();

        JsonMergePatch jsonMergePatchMock = mock(JsonMergePatch.class);
        when(warehouseRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(utilsService.applyPatch(saveItemDto, jsonMergePatchMock, SaveItemDto.class)).thenReturn(saveItemDto);

        // When
        underTest.updateItem(itemId, jsonMergePatchMock);

        // Then
        verify(warehouseRepository, times(1)).findById(itemId);
        verify(utilsService, times(1)).applyPatch(saveItemDto, jsonMergePatchMock, SaveItemDto.class);
    }

    @Test
    void itShouldThrowJsonPatchException() throws JsonPatchException, JsonProcessingException {
        // Given
        Long itemId = 10L;
        Product product = Product.builder()
                .id(itemId)
                .build();

        Item item = Item.builder()
                .id(itemId)
                .product(product)
                .quantity(100L)
                .available(true)
                .build();

        JsonMergePatch jsonMergePatchMock = mock(JsonMergePatch.class);
        when(warehouseRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(utilsService.applyPatch(any(SaveItemDto.class), any(JsonMergePatch.class), eq(SaveItemDto.class))).thenThrow(JsonPatchException.class);

        // When
        // Then
        assertThrows(ServerException.class, () -> underTest.updateItem(itemId, jsonMergePatchMock));
        ArgumentCaptor<SaveItemDto> saveItemDtoArgumentCaptor = ArgumentCaptor.forClass(SaveItemDto.class);
        ArgumentCaptor<JsonMergePatch> jsonMergePatchArgumentCaptor = ArgumentCaptor.forClass(JsonMergePatch.class);
        verify(warehouseRepository, times(1)).findById(itemId);
        verify(utilsService, times(1)).applyPatch(saveItemDtoArgumentCaptor.capture(), jsonMergePatchArgumentCaptor.capture(), eq(SaveItemDto.class));
    }

    @Test
    void itShouldRetrieveItems() {
        // Given
        Long itemId = 10L;
        Item item = Item.builder()
                .id(itemId)
                .available(true)
                .quantity(100L)
                .product(new Product())
                .build();

        int page = 0;
        int size = 5;
        List<Item> items = List.of(item);
        PageImpl<Item> itemsPage = new PageImpl<>(items);
        PageRequest pageRequest = PageRequest.of(page, size, ASC, SORT_ID);
        when(warehouseRepository.findAll(pageRequest)).thenReturn(itemsPage);

        // When
        underTest.getItems(page, size);

        // Then
        verify(warehouseRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void itShouldDeleteItemByGivenId() {
        // Given
        Long itemId = 1L;
        Item item = Item.builder()
                .id(itemId)
                .available(true)
                .quantity(100L)
                .product(new Product())
                .build();
        when(warehouseRepository.findById(itemId)).thenReturn(Optional.of(item));

        // When
        underTest.deleteItem(itemId);

        // Then
        verify(warehouseRepository, times(1)).findById(itemId);
    }

    @Test
    void itShouldThrowItemNoFoundException() {
        // Given
        Long itemId = 1L;
        when(warehouseRepository.findById(itemId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ItemNotFoundException.class, () -> underTest.deleteItem(itemId));
        verify(warehouseRepository, times(1)).findById(itemId);
    }

}