package pl.dk.ecommerceplatform.address;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String postalCode;
    @NotBlank private String street;
    @NotBlank private String buildingNumber;
    @NotBlank private String phoneNumber;
}
