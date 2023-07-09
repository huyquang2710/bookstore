package com.book.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.domain.PasswordResetToken;
import com.book.domain.ShoppingCart;
import com.book.domain.User;
import com.book.domain.UserBilling;
import com.book.domain.UserPayment;
import com.book.domain.UserShipping;
import com.book.domain.security.UserRole;
import com.book.repository.PasswordResetTokenRepository;
import com.book.repository.RoleRepository;
import com.book.repository.UserPaymentRepository;
import com.book.repository.UserRepository;
import com.book.repository.UserShippingReposioty;
import com.book.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserPaymentRepository userPaymentRepository;

	@Autowired
	private UserShippingReposioty userShippingReposioty;

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {

		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);

		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		User localUser = userRepository.findByUsername(user.getUsername());
		if (localUser != null) {
			LOGGER.info("User {} already exist. Nothing will be done!", user.getUsername());
		} else {
			for (UserRole userRole : userRoles) {
				roleRepository.save(userRole.getRole());
			}
			user.getUserRoles().addAll(userRoles);

			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);

			user.setUserShippingList(new ArrayList<UserShipping>());
			user.setUserPaymentList(new ArrayList<UserPayment>());

			localUser = userRepository.save(user);
		}
		return localUser;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);

		userBilling.setUserPayment(userPayment);

		user.getUserPaymentList().add(userPayment);

		save(user);
	}

	@Override
	public void setUserDefaultPayment(Long defaultPaymentId, User user) {
		List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.getId() == defaultPaymentId) {
				userPayment.setDefaultPayment(true);
				userPaymentRepository.save(userPayment);
			} else {
				userPayment.setDefaultPayment(false);
				userPaymentRepository.save(userPayment);
			}
		}
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
	}

	@Override
	public void setUserDefaultShippingAdddress(Long defaultShippingAdddress, User user) {
		List<UserShipping> userShippingList = (List<UserShipping>) userShippingReposioty.findAll();
		for (UserShipping userShipping : userShippingList) {
			if (userShipping.getId() == defaultShippingAdddress) {
				userShipping.setUserShippingDefault(false);
				userShippingReposioty.save(userShipping);
			} else {
				userShipping.setUserShippingDefault(false);
				userShippingReposioty.save(userShipping);
			}
		}
	}

}
