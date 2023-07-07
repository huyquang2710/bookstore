package com.book.service;

import java.util.Set;

import com.book.domain.PasswordResetToken;
import com.book.domain.User;
import com.book.domain.UserBilling;
import com.book.domain.UserPayment;
import com.book.domain.security.UserRole;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetTokenForUser(final User user, final String token);

	User findByUsername(String username);

	User findByEmail(String email);

	User createUser(User user, Set<UserRole> userRoles) throws Exception;

	User save(User user);

	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
}
