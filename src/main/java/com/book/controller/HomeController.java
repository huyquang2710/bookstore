package com.book.controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.book.domain.PasswordResetToken;
import com.book.domain.User;
import com.book.domain.security.Role;
import com.book.domain.security.UserRole;
import com.book.service.UserService;
import com.book.service.impl.UserSecurityService;
import com.book.utility.Constant;
import com.book.utility.MailConstructor;
import com.book.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private MailConstructor mailConstructor;

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
	public String newUserGet(Model model, Locale locale, @RequestParam("token") String token) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

		if (passwordResetToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
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

	@PostMapping("/newUser")
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);

		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("email", true);

			return "myAccount";
		}
		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);

			return "myAccount";
		}
		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);

		// random and encode password
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		// role
		Role role = new Role();
		role.setRoleId(1);
		role.setName(Constant.ROLE_USER);
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));

		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage simpleMailMessage = mailConstructor.construcResetTokenEmail(appUrl, request.getLocale(),
				token, user, password);

		javaMailSender.send(simpleMailMessage);

		model.addAttribute("emailSent", true);

		return "myAccount";
	}
}
