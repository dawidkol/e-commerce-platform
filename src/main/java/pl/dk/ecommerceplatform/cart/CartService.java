package pl.dk.ecommerceplatform.cart;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.cart.dtos.AddToCartDto;
import pl.dk.ecommerceplatform.cart.dtos.CartDto;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.ItemNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.ProductUnavailableException;
import pl.dk.ecommerceplatform.error.exceptions.warehouse.QuantityException;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;
import pl.dk.ecommerceplatform.warehouse.Item;
import pl.dk.ecommerceplatform.warehouse.WarehouseRepository;

import java.util.*;

@Service
@AllArgsConstructor
class CartService {

    private final CartRepository cartRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartDtoMapper cartDtoMapper;

    @Transactional
    public CartDto addProductToCart(AddToCartDto toCartDto) {

        User user = userRepository.findById(toCartDto.userId()).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(toCartDto.productId()).orElseThrow(ProductNotFoundException::new);
        Item item = getItem(product.getId()).orElseThrow(ItemNotFoundException::new);

        if (item.getQuantity() < toCartDto.quantity())
            throw new QuantityException();

        Cart cart;
        Long itemId;
        if (item.isAvailable()) {
            itemId = item.getId();
            cart = this.getCart(toCartDto, user);
        } else
            throw new ProductUnavailableException();

        this.addProduct(toCartDto, cart, product);
        warehouseRepository.updateQuantity(itemId, toCartDto.quantity());
        warehouseRepository.flush();
        cartRepository.flush();
        return cartDtoMapper.map(cart);
    }

    private Cart getCart(AddToCartDto toCartDto, User user) {
        return cartRepository.findByUser_id(toCartDto.userId()).orElseGet(() -> {
            Cart newCart = this.cartBuilder(user);
            return cartRepository.save(newCart);
        });
    }

    private Cart cartBuilder(User user) {
        return Cart.builder()
                .products(new ArrayList<Product>())
                .user(user)
                .build();
    }

    private void addProduct(AddToCartDto toCartDto, Cart cart, Product product) {
        for (int i = 0; i < toCartDto.quantity(); i++)
            cart.getProducts().add(product);
    }

    private Optional<Item> getItem(Long productId) {
        return warehouseRepository.findByProduct_id(productId);
    }

}
