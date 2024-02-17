package pl.dk.ecommerceplatform.statistics;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.util.List;

@Service
@RequiredArgsConstructor
class StatisticsService {

    private final StatisticsDAO statisticsDAO;

    Logger logger = UtilsService.getLogger(this.getClass());

    public List<CartProductsDto> getTop3SoldProducts() {
        List<CartProductsDto> top3SoldProducts = statisticsDAO.getTop3SoldProducts();
        logger.info(top3SoldProducts.toString());
        return top3SoldProducts;
    }

}

