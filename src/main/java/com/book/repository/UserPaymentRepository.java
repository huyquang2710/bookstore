package com.book.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.domain.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {

}
