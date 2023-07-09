package com.book.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.book.domain.Book;
import com.book.domain.CartItem;
import com.book.domain.ShoppingCart;
import com.book.domain.User;
import com.book.service.BookService;
import com.book.service.CartItemService;
import com.book.service.ShoppingCartService;
import com.book.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private BookService bookService;

	@GetMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();

		List<CartItem> cartItems = cartItemService.findByShoppingCart(shoppingCart);

		shoppingCartService.updateShoppingCart(shoppingCart);

		model.addAttribute("cartItemList", cartItems);
		model.addAttribute("shoppingCart", shoppingCart);

		return "shoppingCart";
	}

	@PostMapping("/addItem")
	public String addItem(@ModelAttribute("book") Book book, @ModelAttribute("qty") String qty, Model model,
			Principal principal) {
		User user = userService.findByUsername(principal.getName());
		book = bookService.findOne(book.getId());

		if (Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "redriect:/bookDetail?id=" + book.getId();
		}
		CartItem cartItem = cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);

		return "redirect:/bookDetail?id=" + book.getId();
	}

	@PostMapping("/updateCartItem")
	public String updateCartItem(@ModelAttribute("id") Long cartItemId, @ModelAttribute("qty") int qty) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);

		cartItemService.updateCartItem(cartItem);

		return "redirect:/shoppingCart/cart";
	}

	@GetMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeItem(cartItemService.findById(id));

		return "redirect:/shoppingCart/cart";
	}
}
