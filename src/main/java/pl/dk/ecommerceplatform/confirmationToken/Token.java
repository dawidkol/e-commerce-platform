package pl.dk.ecommerceplatform.confirmationToken;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expiration;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
