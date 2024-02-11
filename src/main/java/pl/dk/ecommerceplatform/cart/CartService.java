package pl.dk.ecommerceplatform.cart;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.cart.dtos.AddToCartDto;
import pl.dk.ecommerceplatform.cart.dtos.CartDto;
import pl.dk.ecommerceplatform.error.exceptions.cart.CartNotFoundException;
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
public class CartService {

    private final CartRepository cartRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartDtoMapper cartDtoMapper;
    private final CartProductsDAO cartProductsDAO;

    @Transactional
    public CartDto addProductToCart(Long userId, AddToCartDto toCartDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        ProductItemChecker productItemChecker = this.validateProductAndItemData(toCartDto);
        Product product = productItemChecker.product;
        Item item = productItemChecker.item;

        Cart cart;
        if (item.isAvailable()) {
            cart = this.getCart(user);
        } else
            throw new ProductUnavailableException();

        this.addProduct(toCartDto, cart, product);
        cartRepository.flush();
        return cartDtoMapper.map(cart);
    }

    @Transactional
    public void updateProductQuantityInCart(Long userId, AddToCartDto dto) {
        Cart cart = cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId).orElseThrow(CartNotFoundException::new);
        this.validateProductAndItemData(dto);

        Long cartId = cart.getId();
        Long productId = cart.getProducts().stream()
                .filter(p -> p.getId().equals(dto.productId()))
                .findFirst()
                .map(Product::getId)
                .orElseThrow(ProductNotFoundException::new);
        cartProductsDAO.deleteProductsInCart(cartId, productId);
        for (int i = 0; i < dto.quantity(); i++) {
            cartProductsDAO.insertProductToCart(cartId, productId);
        }
    }

    private ProductItemChecker validateProductAndItemData(AddToCartDto dto) {
        Product product = productRepository.findById(dto.productId()).orElseThrow(ProductNotFoundException::new);
        Item item = this.getItem(product.getId()).orElseThrow(ItemNotFoundException::new);
        if (item.getQuantity() < dto.quantity())
            throw new QuantityException("Insufficient stock of the product in the warehouse. Product id = %d" .formatted(product.getId()));
        return new ProductItemChecker(product, item);
    }

    @Transactional
    public void cleanUserCart(Long userId) {
        cartRepository.findCartByUserIdWhereUsedEqualsFalse(userId)
                .ifPresentOrElse(cartRepository::delete, CartNotFoundException::new);
    }

    public Cart getCart(User user) {
        return cartRepository.findCartByUserIdWhereUsedEqualsFalse(user.getId()).orElseGet(() -> {
            Cart newCart = this.cartBuilder(user);
            return cartRepository.save(newCart);
        });
    }

    private Cart cartBuilder(User user) {
        return Cart.builder()
                .products(new ArrayList<Product>())
                .user(user)
                .used(false)
                .build();
    }

    private void addProduct(AddToCartDto toCartDto, Cart cart, Product product) {
        for (int i = 0; i < toCartDto.quantity(); i++)
            cart.getProducts().add(product);
    }

    private Optional<Item> getItem(Long productId) {
        return warehouseRepository.findByProduct_id(productId);
    }

    private record ProductItemChecker(Product product, Item item) {
    }
}

