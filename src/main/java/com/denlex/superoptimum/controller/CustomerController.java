package com.denlex.superoptimum.controller;

import com.denlex.superoptimum.domain.location.City;
import com.denlex.superoptimum.domain.product.*;
import com.denlex.superoptimum.domain.user.Customer;
import com.denlex.superoptimum.service.product.CategoryService;
import com.denlex.superoptimum.service.product.StoreItemService;
import com.denlex.superoptimum.service.product.SubcategoryService;
import com.denlex.superoptimum.service.user.CartItemService;
import com.denlex.superoptimum.service.user.CartService;
import com.denlex.superoptimum.service.user.CustomerService;
import com.denlex.superoptimum.service.user.impl.CustomerCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Shishkov A.V. on 19.08.18.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SubcategoryService subcategoryService;

	@Autowired
	@Qualifier("customerCredentialsService")
	private CustomerCredentialsService credentialsService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private StoreItemService storeItemService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@GetMapping("/loading")
	public String showLoadingPage() {
		return "customer/loading";
	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "customer/login";
	}

	@GetMapping("/main")
	public String showMainPage(Principal principal, Model model, HttpSession session) {
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);

		Customer currentCustomer = customerService.findByUsername(principal.getName());
		Cart activeCart = customerService.getActiveCustomerCart(currentCustomer.getId());

		model.addAttribute("customer", currentCustomer);
		model.addAttribute("cart", activeCart);

		session.setAttribute("customer", currentCustomer);
		session.setAttribute("cart", activeCart);
		return "customer/main";
	}

	@GetMapping("/products/category/{categoryId}")
	public String showProductsByCategory(@PathVariable Long categoryId, Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		model.addAttribute("customer", customer);

		Cart activeCart = (Cart) session.getAttribute("cart");
		model.addAttribute("cart", activeCart);

		Category category = categoryService.findById(categoryId);
		model.addAttribute("category", category);

		City customerCity = customer.getContact().getAddress().getCity();
		List<StoreItem> storeItems = storeItemService.findAllByCategoryAndCity(categoryId, customerCity.getId());
		model.addAttribute("storeItems", storeItems);

		return "customer/category_products";
	}

	@GetMapping("/products/subcategory/{subcategoryId}")
	public String showProductsBySubcategory(@PathVariable Long subcategoryId, Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		model.addAttribute("customer", customer);

		Cart activeCart = (Cart) session.getAttribute("cart");
		model.addAttribute("cart", activeCart);

		Subcategory subcategory = subcategoryService.findById(subcategoryId);
		model.addAttribute("subcategory", subcategory);

		City customerCity = customer.getContact().getAddress().getCity();
		List<StoreItem> storeItems = storeItemService.findAllBySubcategoryAndCity(subcategoryId, customerCity.getId());
		model.addAttribute("storeItems", storeItems);

		return "customer/subcategory_products";
	}

	@GetMapping("/products/product/{productId}")
	public String showProductDetails(@PathVariable Long productId, Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		model.addAttribute("customer", customer);

		Cart activeCart = (Cart) session.getAttribute("cart");
		model.addAttribute("cart", activeCart);

		StoreItem storeItem = storeItemService.findById(productId);
		model.addAttribute("storeItem", storeItem);
		model.addAttribute("subcategory", storeItem.getProduct().getSubcategory());
		model.addAttribute("category", storeItem.getProduct().getSubcategory().getCategory());

		return "customer/product_details";
	}

	@GetMapping("/cart")
	public String showCart(Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		model.addAttribute("customer", customer);

		Cart activeCart = (Cart) session.getAttribute("cart");
		model.addAttribute("cart", activeCart);

		return "customer/cart";
	}

	@PostMapping(value = "/cart/add_item",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public ResponseEntity<Cart> addCartItem(@RequestBody com.denlex.superoptimum.dto.CartItem item, HttpSession session) {
		Cart activeCart = (Cart) session.getAttribute("cart");

		if (activeCart == null) {
			return new ResponseEntity<>((Cart) null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		activeCart = cartService.findById(activeCart.getId());
		StoreItem storeItem = storeItemService.findById(item.getStoreItemId());

		if (activeCart.getItems().isEmpty()) {
			activeCart.addItems(new CartItem(storeItem, item.getQuantity()));
		} else {
			Optional<CartItem> result = activeCart.getItems()
					.stream().filter(cartItem -> cartItem.getItem().getId() == item.getStoreItemId()).findFirst();

			if (!result.isPresent()) {
				activeCart.addItems(new CartItem(storeItem, item.getQuantity()));
			} else {
				CartItem cartItem = result.get();
				cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
			}
		}

		activeCart = cartService.save(activeCart);
		return new ResponseEntity<>(activeCart, HttpStatus.OK);
	}

	private String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return currentUserName;
		}
		return null;
	}
}
