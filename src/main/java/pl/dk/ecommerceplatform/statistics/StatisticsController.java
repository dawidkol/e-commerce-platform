package pl.dk.ecommerceplatform.statistics;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dk.ecommerceplatform.statistics.dtos.AvgOrderDto;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.util.List;

@RestController
@RequestMapping("/stats")
@AllArgsConstructor
class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("")
    public ResponseEntity<List<CartProductsDto>> getTop3SoldProducts() {
        List<CartProductsDto> top10SoldProducts = statisticsService.getTop3SoldProducts();
        return ResponseEntity.ok(top10SoldProducts);
    }

    @GetMapping("/avg")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<AvgOrderDto> getAverageStatsFromLastMonth() {
        AvgOrderDto averageStatsFromLastMonth = statisticsService.getStatsFromLastMonth();
        return ResponseEntity.ok(averageStatsFromLastMonth);
    }
}
