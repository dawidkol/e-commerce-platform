package pl.dk.ecommerceplatform.address;

import jakarta.persistence.*;
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
    private String postalCode;
    private String street;
    private String buildingNumber;
    private String phoneNumber;
}
