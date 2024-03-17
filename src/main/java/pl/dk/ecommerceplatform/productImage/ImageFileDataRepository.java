package pl.dk.ecommerceplatform.productImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageFileDataRepository extends JpaRepository<ImageFileData, Long> {
    List<ImageFileData> findAllByProduct_id(Long id);
}
