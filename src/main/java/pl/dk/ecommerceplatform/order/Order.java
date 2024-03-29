package pl.dk.ecommerceplatform.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.address.Address;
import pl.dk.ecommerceplatform.cart.Cart;
import pl.dk.ecommerceplatform.shipping.Shipping;
import pl.dk.ecommerceplatform.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    @OneToOne
    @JoinColumn(name = "cart_id", unique = true)
    @NotNull
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "shipping_id")
    @NotNull
    private Shipping shipping;
    @ManyToOne
    @JoinColumn(name = "address_id")
    @NotNull
    private Address address;
    @NotNull
    private BigDecimal orderValue;
    @NotNull
    @PastOrPresent
    private LocalDateTime created;

}
