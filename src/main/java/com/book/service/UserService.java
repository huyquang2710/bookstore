package com.book.service;

import com.book.domain.PasswordResetToken;
import com.book.domain.User;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUser(String username);

	User findByEmail(String email);
}
