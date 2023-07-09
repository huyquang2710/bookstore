package com.book.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.book.domain.BillingAddress;
import com.book.domain.CartItem;
import com.book.domain.Payment;
import com.book.domain.ShippingAddress;
import com.book.domain.ShoppingCart;
import com.book.domain.User;
import com.book.domain.UserPayment;
import com.book.domain.UserShipping;
import com.book.service.BillingAddressService;
import com.book.service.CartItemService;
import com.book.service.PaymentService;
import com.book.service.ShippingAddressService;
import com.book.service.UserService;
import com.book.utility.Constant;

@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private BillingAddressService billingAddressService;

	@GetMapping("/checkout")
	public String checkout(Model model, Principal principal, @RequestParam("id") Long cartId,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField) {

		User user = userService.findByUsername(principal.getName());
		if (cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}
		List<CartItem> cartItems = cartItemService.findByShoppingCart(user.getShoppingCart());

		if (cartItems.size() == 0) {
			model.addAttribute("notEnougtStock", true);
			return "redirect:/shoppingCart/cart";
		}
		for (CartItem cartItem : cartItems) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				return "redirect:/shoppingCart/cart";
			}
		}

		List<UserShipping> userShippings = user.getUserShippingList();
		List<UserPayment> userPayments = user.getUserPaymentList();

		model.addAttribute("userShippingList", userShippings);
		model.addAttribute("userPaymentList", userPayments);

		if (userPayments.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}

		if (userShippings.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}

		for (UserShipping userShipping : userShippings) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}

		for (UserPayment userPayment : userPayments) {
			if (userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}

		ShoppingCart shoppingCart = user.getShoppingCart();

		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItems);
		model.addAttribute("shoppingCart", shoppingCart);

		List<String> stateList = Constant.listOFStatiesCode;
		Collections.sort(stateList);

		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveShipping", true);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return "checkout";
	}

}
