package pl.dk.ecommerceplatform.confirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);
}
