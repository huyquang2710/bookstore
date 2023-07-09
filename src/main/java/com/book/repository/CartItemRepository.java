package com.book.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.book.domain.CartItem;
import com.book.domain.ShoppingCart;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
}
