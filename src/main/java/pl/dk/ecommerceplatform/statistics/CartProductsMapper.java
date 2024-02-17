package pl.dk.ecommerceplatform.statistics;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
class CartProductsMapper implements RowMapper<CartProductsDto> {

    @Override
    public CartProductsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        long productId = rs.getLong(1);
        String uriString = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/products/{id}").buildAndExpand(productId)
                .toUriString();
        String productName = rs.getString(2);
        long amountOfSoldProducts = rs.getLong(3);

        return new CartProductsDto(productName, amountOfSoldProducts, uriString);
    }

}


