package com.book.service;

import com.book.domain.UserPayment;

public interface UserPaymentsService {
	UserPayment findOne(Long id);

	void removeById(Long id);
}
