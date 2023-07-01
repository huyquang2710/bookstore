package com.book.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.book.domain.PasswordResetToken;
import com.book.domain.User;
import com.book.service.UserService;
import com.book.service.impl.UserSecurityService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}

	@GetMapping("/forgetPassword")
	public String forgetPassword(Model model) {

		model.addAttribute("classActiveForgetPassword", true);
		return "myAccount";
	}

	@GetMapping("/newUser")
	public String newUser(Model model, Locale locale, @RequestParam("token") String token) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

		if (passwordResetToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/bacRequest";
		}

		User user = passwordResetToken.getUser();
		
		String username = user.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("classActiveEdit", true);
		return "myProfile";
	}
}
