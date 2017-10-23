package com.stala.grzegorz.service.impl;

import com.stala.grzegorz.model.*;
import com.stala.grzegorz.repository.ProductToCartItemRepository;
import com.stala.grzegorz.repository.CartItemRepository;
import com.stala.grzegorz.repository.SizeRepository;
import com.stala.grzegorz.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductToCartItemRepository productToCartItemRepository;

    @Autowired
    private SizeRepository sizeRepository;

    public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
        return cartItemRepository.findByShoppingCart(shoppingCart);
    }

    public CartItem updateCartItem(CartItem cartItem) {
        BigDecimal bigDecimal = new BigDecimal(cartItem.getProduct().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));

        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        cartItem.setSubtotal(bigDecimal);

        cartItemRepository.save(cartItem);

        return cartItem;
    }

    public CartItem addProductToCartItem(Product product, User user, int qty, double sizeEUR) {
        List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
        Size size = sizeRepository.findBySizeEUR(sizeEUR);

        for (CartItem cartItem : cartItemList) {
            if ((product.getId() == cartItem.getProduct().getId()) && (cartItem.getSize().getSizeEUR() == sizeEUR)) {
                cartItem.setQty(cartItem.getQty() + qty);
                cartItem.setSubtotal(new BigDecimal(product.getOurPrice()).multiply(new BigDecimal(qty)));
                cartItemRepository.save(cartItem);
                return cartItem;
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(user.getShoppingCart());
        cartItem.setProduct(product);
        cartItem.setSize(size);

        cartItem.setQty(qty);
        cartItem.setSubtotal(new BigDecimal(product.getOurPrice()).multiply(new BigDecimal(qty)));
        cartItem = cartItemRepository.save(cartItem);

        ProductToCartItem productToCartItem = new ProductToCartItem();
        productToCartItem.setProduct(product);
        productToCartItem.setCartItem(cartItem);
        productToCartItemRepository.save(productToCartItem);

        return cartItem;
    }

    public CartItem findById(Long id) {
        return cartItemRepository.findOne(id);
    }

    public void removeCartItem(CartItem cartItem) {
        productToCartItemRepository.deleteByCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> findByOrder(Order order) {
        return cartItemRepository.findByOrder(order);
    }
}
