package com.stala.grzegorz.service.impl;

import com.stala.grzegorz.model.*;
import com.stala.grzegorz.repository.OrderRepository;
import com.stala.grzegorz.service.CartItemService;
import com.stala.grzegorz.service.OrderService;
import com.stala.grzegorz.utility.ShippingMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemService cartItemService;

    public synchronized Order createOrder(ShoppingCart shoppingCart,
                                          ShippingAddress shippingAddress,
                                          String shippingMethod,
                                          String paymentMethod,
                                          User user) {
        Order order = new Order();
        order.setOrderStatus("created");
        order.setShippingAddress(shippingAddress);
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(paymentMethod);

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();
            cartItem.setOrder(order);
            product.setInStockNumber(product.getInStockNumber() - cartItem.getQty());
        }

        order.setCartItemList(cartItemList);
        order.setOrderDate(Calendar.getInstance().getTime());
        shippingAddress.setOrder(order);
        order.setUser(user);

        LocalDate today = LocalDate.now();
        LocalDate estimatedDeliveryDate = null;
        double deliveryCost = 0;

        if (shippingMethod.equals(ShippingMethod.ECONOMIC_PARCEL.name())) {
            estimatedDeliveryDate = today.plusDays(ShippingMethod.ECONOMIC_PARCEL.getEstimatedDelivery());
            deliveryCost += ShippingMethod.ECONOMIC_PARCEL.getCost();
        } else if (shippingMethod.equals(ShippingMethod.PRIORITY_PARCEL.name())) {
            estimatedDeliveryDate = today.plusDays(ShippingMethod.PRIORITY_PARCEL.getEstimatedDelivery());
            deliveryCost += ShippingMethod.PRIORITY_PARCEL.getCost();
        }
        order.setOrderTotal(shoppingCart.getGrandTotal().add(new BigDecimal(deliveryCost)));
        order.setShippingDate(java.sql.Date.valueOf(estimatedDeliveryDate));

        order = orderRepository.save(order);

        return order;
    }

    public Order findOne(Long id) {
        return orderRepository.findOne(id);
    }

}
