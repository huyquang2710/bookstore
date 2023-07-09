package com.book.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.book.domain.Order;
import com.book.domain.User;

@Component
public class MailConstructor {
	@Autowired
	private Environment environment;

	@Autowired
	private TemplateEngine templateEngine;

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

	public MimeMessagePreparator construcOrderConfirmationEmail(User user, Order order, Locale locale) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);

		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
				messageHelper.setTo(user.getEmail());
				messageHelper.setSubject("Order Confirmation - " + order.getId());
				messageHelper.setText(text, true);
				messageHelper.setFrom(new InternetAddress("huyquang271099@gmail.com"));
			}
		};

		return messagePreparator;
	}
}
