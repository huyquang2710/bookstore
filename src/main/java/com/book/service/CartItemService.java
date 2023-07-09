package com.book.service;

import java.util.List;

import com.book.domain.Book;
import com.book.domain.CartItem;
import com.book.domain.ShoppingCart;
import com.book.domain.User;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

	CartItem updateCartItem(CartItem cartItem);

	CartItem addBookToCartItem(Book book, User user, int qty);

	CartItem findById(Long id);
	
	void removeItem(CartItem cartItem);
}
