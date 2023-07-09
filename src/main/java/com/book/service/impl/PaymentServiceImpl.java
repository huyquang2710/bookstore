package com.book.service.impl;

import org.springframework.stereotype.Service;

import com.book.domain.Payment;
import com.book.domain.UserPayment;
import com.book.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
		payment.setType(userPayment.getType());
		payment.setHolderName(userPayment.getHolderName());
		payment.setCardName(userPayment.getCardNumber());
		payment.setExpiryMonth(userPayment.getExpiryMonth());
		payment.setExpiryYear(userPayment.getExpiryYear());
		payment.setCvc(userPayment.getCvc());

		return payment;
	}

}
