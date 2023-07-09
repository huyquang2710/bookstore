package com.book.service;

import com.book.domain.ShippingAddress;
import com.book.domain.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
