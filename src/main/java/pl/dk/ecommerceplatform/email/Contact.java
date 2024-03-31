package pl.dk.ecommerceplatform.email;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Email
    private String email;
    @NotBlank
    private String subject;
    @NotBlank
    @Size(min = 10, max = 10000)
    private String message;
    @NotNull
    private LocalDateTime dateOfPosting;
    private LocalDateTime replyDate;
    @Size(min = 10, max = 10000)
    private String replyMessage;

}
