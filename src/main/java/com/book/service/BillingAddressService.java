package com.book.service;

import com.book.domain.BillingAddress;
import com.book.domain.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
