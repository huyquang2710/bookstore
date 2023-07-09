package com.book.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.domain.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
