package pl.dk.ecommerceplatform.email;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime dateOfPosting;
    private LocalDateTime replyDate;

}
