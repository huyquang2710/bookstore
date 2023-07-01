package com.book.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.domain.PasswordResetToken;
import com.book.domain.User;
import com.book.repository.PasswordResetTokenRepository;
import com.book.repository.UserRepository;
import com.book.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private UserRepository userRepository;

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
	public User findByUser(String username) {
		return userRepository.findByUsername(username);
	}

}
