package com.book.utility;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.book.domain.User;

@Component
public class MailConstructor {
	@Autowired
	private Environment environment;

	public SimpleMailMessage construcResetTokenEmail(String contextPath, Locale locale, String token, User user,
			String password) {
		String url = contextPath + "/newUser?token=" + token;
		String message = "\nPlease click on this link to verify your email and edit your personal infomation. Your password is: \n"
				+ password;
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Bookstore - New User");
		simpleMailMessage.setText(url + message);
		simpleMailMessage.setFrom(environment.getProperty("support.email"));

		return simpleMailMessage;
	}
}
