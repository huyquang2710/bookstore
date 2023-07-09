package com.book.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.domain.BillingAddress;
import com.book.domain.Book;
import com.book.domain.CartItem;
import com.book.domain.Order;
import com.book.domain.Payment;
import com.book.domain.ShippingAddress;
import com.book.domain.ShoppingCart;
import com.book.domain.User;
import com.book.repository.OrderReposioty;
import com.book.service.CartItemService;
import com.book.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderReposioty orderReposioty;

	@Autowired
	private CartItemService cartItemService;

	@Override
	public synchronized Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress,
			Payment payment, String shippingMethod, User user) {
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setOrderStatus("created");
		order.setPayment(payment);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);

		List<CartItem> cartItems = cartItemService.findByShoppingCart(shoppingCart);

		for (CartItem cartItem : cartItems) {
			Book book = cartItem.getBook();
			cartItem.setOrder(order);
			book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
		}

		order.setCartItemList(cartItems);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		payment.setOrder(order);
		order.setUser(user);
		order = orderReposioty.save(order);

		return order;
	}

	@Override
	public Order findOne(Long id) {
		return orderReposioty.findById(id).orElse(null);
	}

}
