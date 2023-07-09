package com.book.service;

import com.book.domain.BillingAddress;
import com.book.domain.Order;
import com.book.domain.Payment;
import com.book.domain.ShippingAddress;
import com.book.domain.ShoppingCart;
import com.book.domain.User;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress,
			Payment payment, String shippingMethod, User user);

	Order findOne(Long id);
}
