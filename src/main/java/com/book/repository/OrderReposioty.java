package com.book.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.domain.Order;

public interface OrderReposioty extends CrudRepository<Order, Long> {

}
