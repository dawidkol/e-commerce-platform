package pl.dk.ecommerceplatform.confirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query(value = "SELECT token.* FROM token JOIN users ON token.user_id = users.id WHERE expiration > NOW() AND users.email= :userEmail", nativeQuery = true)
    Optional<Token> findTokenByUser_Email(String userEmail);
}
