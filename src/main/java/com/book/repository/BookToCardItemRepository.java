package com.book.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.domain.BookToCartItem;

public interface BookToCardItemRepository extends CrudRepository<BookToCartItem, Long> {

}
