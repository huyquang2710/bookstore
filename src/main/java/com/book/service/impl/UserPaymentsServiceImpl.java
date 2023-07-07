package com.book.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.domain.UserPayment;
import com.book.repository.UserPaymentRepository;
import com.book.service.UserPaymentsService;

@Service
public class UserPaymentsServiceImpl implements UserPaymentsService {

	@Autowired
	private UserPaymentRepository userPaymentRepository;

	@Override
	public UserPayment findOne(Long id) {

		return userPaymentRepository.findById(id).orElse(null);
	}

	@Override
	public void removeById(Long id) {
		userPaymentRepository.deleteById(id);
	}

}
