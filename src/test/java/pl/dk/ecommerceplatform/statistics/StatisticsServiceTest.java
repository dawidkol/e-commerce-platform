package pl.dk.ecommerceplatform.statistics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.dk.ecommerceplatform.statistics.dtos.AvgOrderDto;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsServiceTest {

    @Mock
    private StatisticsDAO statisticsDAO;
    private StatisticsService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StatisticsService(statisticsDAO);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldGetTop3SoldProducts() {
        // Given
        List<CartProductsDto> expectedProducts = new ArrayList<>();
        expectedProducts.add(new CartProductsDto("Product 1", 10L, "url1"));
        expectedProducts.add(new CartProductsDto("Product 2", 8L, "url2"));
        expectedProducts.add(new CartProductsDto("Product 3", 6L, "url3"));

        when(statisticsDAO.getTop3SoldProducts()).thenReturn(expectedProducts);

        // When
        List<CartProductsDto> actualProducts = underTest.getTop3SoldProducts();

        // Then
        assertEquals(expectedProducts.size(), actualProducts.size());
        verify(statisticsDAO, times(1)).getTop3SoldProducts();
    }

    @Test
    void itShouldGetStatsFromLastMonth() {
        // Given
        AvgOrderDto expectedDto = AvgOrderDto.builder()
                .avgOrderValue(BigDecimal.valueOf(99.99))
                .amountOfOrders(3L)
                .totalSoldProducts(12L)
                .averageProductsPerOrder(BigDecimal.valueOf(4.00))
                .build();

        when(statisticsDAO.getStatsFromLastMonth()).thenReturn(expectedDto);

        // When
        underTest.getStatsFromLastMonth();

        // THen
        verify(statisticsDAO, times(1)).getStatsFromLastMonth();
    }

}