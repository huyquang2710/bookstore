package com.book.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.book.domain.PasswordResetToken;
import com.book.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

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
	public String forgetPassword(Model model, Locale locale, @RequestParam("token") String token) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

		model.addAttribute("classActiveForgetPassword", true);
		return "myAccount";
	}

	@GetMapping("/newUser")
	public String newUser(Model model) {
		model.addAttribute("classActiveNewUser", true);
		return "myAccount";
	}
}
