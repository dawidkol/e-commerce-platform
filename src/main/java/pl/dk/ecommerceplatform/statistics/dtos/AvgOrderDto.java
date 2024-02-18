package pl.dk.ecommerceplatform.statistics.dtos;

import lombok.Builder;
import lombok.Setter;

import java.math.BigDecimal;
@Builder
public record AvgOrderDto (BigDecimal avgOrderValue, Long amountOfOrders, Long totalSoldProducts, BigDecimal averageProductsPerOrder) {
}
