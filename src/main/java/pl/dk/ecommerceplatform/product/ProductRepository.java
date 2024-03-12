package pl.dk.ecommerceplatform.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT product.* FROM product JOIN category ON product.category_id = category.id" +
                   " WHERE LOWER(product.name) LIKE LOWER(CONCAT('%', :name, '%'))", nativeQuery = true)
    List<Product> findAllByName(String name);

    @Query(value = "SELECT product.* FROM product JOIN category ON product.category_id = category.id " +
                   "WHERE LOWER(product.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(category.name)" +
                   " LIKE LOWER(:category)", nativeQuery = true)
    List<Product> findByNameAndCategory(String name, String category);

    Page<Product> findAllByCategory_Name(String categoryName, Pageable pageable);

    Optional<Product> findByName(String name);

    @Query(value = "SELECT * FROM product WHERE price > promotion_price" +
                   " LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.pageNumber * 10}", nativeQuery = true)
    List<Product> findAllPromotionProducts(@Param("pageable") Pageable pageable);

}
