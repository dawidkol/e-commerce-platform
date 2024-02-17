package pl.dk.ecommerceplatform.statistics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsServiceTest {

    @Mock
    private StatisticsDAO statisticsDAO;
    private StatisticsService statisticsService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        statisticsService = new StatisticsService(statisticsDAO);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getTop3SoldProducts() {
        // Given
        List<CartProductsDto> expectedProducts = new ArrayList<>();
        expectedProducts.add(new CartProductsDto("Product 1", 10L, "url1"));
        expectedProducts.add(new CartProductsDto("Product 2", 8L, "url2"));
        expectedProducts.add(new CartProductsDto("Product 3", 6L, "url3"));

        when(statisticsDAO.getTop3SoldProducts()).thenReturn(expectedProducts);

        // When
        List<CartProductsDto> actualProducts = statisticsService.getTop3SoldProducts();

        // Then
        assertEquals(expectedProducts.size(), actualProducts.size());
        verify(statisticsDAO, times(1)).getTop3SoldProducts();
    }

}