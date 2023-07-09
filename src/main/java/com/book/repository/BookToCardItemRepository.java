package com.book.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.book.domain.BookToCartItem;
import com.book.domain.CartItem;

@Transactional
public interface BookToCardItemRepository extends CrudRepository<BookToCartItem, Long> {

	void deleteByCartItem(CartItem cartItem);

}
