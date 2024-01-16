package pl.dk.ecommerceplatform.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT product.* FROM product JOIN category ON product.category_id = category.id WHERE LOWER(product.name) LIKE LOWER(CONCAT('%', :name, '%'))", nativeQuery = true)
    List<Product> findByName(String name);

    @Query(value = "SELECT product.* FROM product JOIN category ON product.category_id = category.id WHERE LOWER(product.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(category.name) LIKE LOWER(:category)", nativeQuery = true)
    List<Product> findByNameAndCategory(String name, String category);

}
