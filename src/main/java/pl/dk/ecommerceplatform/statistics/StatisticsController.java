package pl.dk.ecommerceplatform.statistics;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.dk.ecommerceplatform.statistics.dtos.AvgOrderDto;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stats")
@AllArgsConstructor
@Validated
class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("")
    public ResponseEntity<List<CartProductsDto>> getTop3SoldProducts() {
        List<CartProductsDto> top10SoldProducts = statisticsService.getTop3SoldProducts();
        return ResponseEntity.ok(top10SoldProducts);
    }

    @GetMapping("/avg")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<AvgOrderDto> getAverageStatsFromLastMonth(
            @Past @RequestParam(name = "start", required = false) LocalDate startDate,
            @PastOrPresent @RequestParam(name = "end", required = false) LocalDate endDate) {
        if (startDate == null && endDate == null) {
            AvgOrderDto averageStatsFromLastMonth = statisticsService.getStatsFromLastMonth();
            return ResponseEntity.ok(averageStatsFromLastMonth);
        } else {
            AvgOrderDto statsFromDate = statisticsService.getStatsFromDate(startDate, endDate);
            return ResponseEntity.ok(statsFromDate);
        }
    }
}
