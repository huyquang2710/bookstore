package com.book.service;

import com.book.domain.Payment;
import com.book.domain.UserPayment;

public interface PaymentService {
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
