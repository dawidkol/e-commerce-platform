package pl.dk.ecommerceplatform.confirmationToken;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query(value = "SELECT token.* FROM token JOIN users ON token.user_id = users.id WHERE expiration > NOW() AND users.email= :userEmail", nativeQuery = true)
    Optional<Token> findTokenByUser_Email(String userEmail);

    @Query(value = "DELETE FROM token WHERE expiration > NOW()", nativeQuery = true)
    void deleteAllInactiveTokens();

    @Query(value = "SELECT * FROM token WHERE expiration > NOW()", nativeQuery = true)
    List<Token> findAllExpiredTokens();

}
