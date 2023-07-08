package com.book.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.domain.UserShipping;
import com.book.repository.UserShippingReposioty;
import com.book.service.UserShippingService;

@Service
public class UserShippingServiceImpl implements UserShippingService {

	@Autowired
	private UserShippingReposioty userShippingReposioty;

	@Override
	public UserShipping findOne(Long id) {
		return userShippingReposioty.findById(id).orElse(null);
	}

	@Override
	public void removeById(Long id) {
		userShippingReposioty.deleteById(id);
	}

}
