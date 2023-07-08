package com.book.service;

import com.book.domain.UserShipping;

public interface UserShippingService {
	UserShipping findOne(Long id);
	
	void removeById(Long id);
}
