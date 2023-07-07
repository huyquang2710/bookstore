package com.book.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

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

import com.book.domain.Book;
import com.book.domain.PasswordResetToken;
import com.book.domain.User;
import com.book.domain.UserBilling;
import com.book.domain.UserPayment;
import com.book.domain.UserShipping;
import com.book.domain.security.Role;
import com.book.domain.security.UserRole;
import com.book.service.BookService;
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

	@Autowired
	private BookService bookService;

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
	public String forgetPassword(Model model, HttpServletRequest request, @ModelAttribute("email") String email) {

		model.addAttribute("classActiveForgetPassword", true);

		User user = userService.findByEmail(email);

		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}

		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		userService.save(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage newEmail = mailConstructor.construcResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		javaMailSender.send(newEmail);

		model.addAttribute("forgetPasswordEmailSent", "true");

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
			model.addAttribute("emailExists", true);

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

	@GetMapping("/bookshelf")
	public String bookshelf(Model model) {
		List<Book> bookList = bookService.findAll();

		model.addAttribute("bookList", bookList);
		return "bookshelf";
	}

	@GetMapping("/bookDetail")
	public String bookdetail(Model model, @PathParam("id") Long id, Principal principal) {
		List<Book> bookList = bookService.findAll();

		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		Book book = bookService.findOne(id);

		model.addAttribute("book", book);

		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		return "bookDetail";
	}

	@GetMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		model.addAttribute("user", user);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);

		List<String> stateList = Constant.listOFStatiesCode;
		Collections.sort(stateList);

		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);

		return "myProfile";
	}

	@GetMapping("/listOfCreditCards")
	public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);

		return "myProfile";
	}

	@GetMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/* model.addAttribute("orderList", user.orderList()); */

		model.addAttribute("listOfCreditcards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);

		return "myProfile";
	}

	@GetMapping("/addNewCreditCard")
	public String addNewCreditCardGet(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);

		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();

		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);

		List<String> stateList = Constant.listOFStatiesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/* model.addAttribute("orderList", user.orderList()); */

		return "myProfile";
	}

	@PostMapping("/addNewCreditCard")
	public String addNewCreditCardPost(@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling, Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);

		return "myProfile";
	}

	@GetMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);

		UserShipping userShipping = new UserShipping();

		model.addAttribute("userShipping", userShipping);

		List<String> stateList = Constant.listOFStatiesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/* model.addAttribute("orderList", user.orderList()); */

		return "myProfile";
	}
}
