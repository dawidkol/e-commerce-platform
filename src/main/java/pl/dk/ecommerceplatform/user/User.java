package pl.dk.ecommerceplatform.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 75)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 75)
    private String lastName;
    @Column(unique = true)
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 200)
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole userRole;
}