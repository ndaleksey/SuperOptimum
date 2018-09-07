package com.denlex.superoptimum.controller;

import com.denlex.superoptimum.domain.location.City;
import com.denlex.superoptimum.domain.product.*;
import com.denlex.superoptimum.domain.user.Customer;
import com.denlex.superoptimum.exception.CartNotFoundException;
import com.denlex.superoptimum.exception.CustomerNotFoundInSessionException;
import com.denlex.superoptimum.service.product.CategoryService;
import com.denlex.superoptimum.service.product.StoreItemService;
import com.denlex.superoptimum.service.product.StoreService;
import com.denlex.superoptimum.service.product.SubcategoryService;
import com.denlex.superoptimum.service.user.CartItemService;
import com.denlex.superoptimum.service.user.CartService;
import com.denlex.superoptimum.service.user.CustomerService;
import com.denlex.superoptimum.service.user.impl.CustomerCredentialsService;
import com.denlex.superoptimum.util.StoreItemByDistributorComparator;
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
import java.util.*;
import java.util.stream.Collectors;

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
	private StoreService storeService;

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
		Cart activeCart = customerService.findActiveCartForCustomer(currentCustomer.getId());

		model.addAttribute("customer", currentCustomer);
		model.addAttribute("cart", activeCart);

		session.setAttribute("customer", currentCustomer);
		session.setAttribute("cart", activeCart);
		return "customer/main";
	}

	@GetMapping("/products/category/{categoryId}")
	public String showProductsByCategory(@PathVariable Long categoryId, Model model,
										 HttpSession session) throws CustomerNotFoundInSessionException {
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) throw new CustomerNotFoundInSessionException();

		model.addAttribute("customer", customer);

		Cart activeCart = customerService.findActiveCartForCustomer(customer.getId());
		model.addAttribute("cart", activeCart);

		Category category = categoryService.findById(categoryId);
		model.addAttribute("category", category);

		City customerCity = customer.getContact().getAddress().getCity();
		List<StoreItem> storeItems = storeItemService.findAllByCategoryAndCity(categoryId, customerCity.getId());
		model.addAttribute("storeItems", storeItems);

		return "customer/category_products";
	}

	@GetMapping("/products/subcategory/{subcategoryId}")
	public String showProductsBySubcategory(@PathVariable Long subcategoryId, Model model,
											HttpSession session) throws CustomerNotFoundInSessionException {
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) throw new CustomerNotFoundInSessionException();

		model.addAttribute("customer", customer);

		Cart activeCart = customerService.findActiveCartForCustomer(customer.getId());
		model.addAttribute("cart", activeCart);

		Subcategory subcategory = subcategoryService.findById(subcategoryId);
		model.addAttribute("subcategory", subcategory);

		City customerCity = customer.getContact().getAddress().getCity();
		List<StoreItem> storeItems = storeItemService.findAllBySubcategoryAndCity(subcategoryId, customerCity.getId());
		model.addAttribute("storeItems", storeItems);

		return "customer/subcategory_products";
	}

	@GetMapping("/products/product/{productId}")
	public String showProductDetails(@PathVariable Long productId, Model model, HttpSession session)
			throws CustomerNotFoundInSessionException {
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) throw new CustomerNotFoundInSessionException();

		model.addAttribute("customer", customer);

		Cart activeCart = customerService.findActiveCartForCustomer(customer.getId());
		model.addAttribute("cart", activeCart);

		StoreItem storeItem = storeItemService.findById(productId);
		model.addAttribute("storeItem", storeItem);
		model.addAttribute("subcategory", storeItem.getProduct().getSubcategory());
		model.addAttribute("category", storeItem.getProduct().getSubcategory().getCategory());

		return "customer/product_details";
	}

	@GetMapping("/cart")
	public String showCart(Model model, HttpSession session) throws CustomerNotFoundInSessionException {
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) throw new CustomerNotFoundInSessionException();

		model.addAttribute("customer", customer);

		Cart activeCart = customerService.findActiveCartForCustomer(customer.getId());
		model.addAttribute("cart", activeCart);

		return "customer/cart";
	}

	@GetMapping("/all_products")
	public String showAllProducts(Model model, HttpSession session) throws CustomerNotFoundInSessionException, CartNotFoundException {
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) throw new CustomerNotFoundInSessionException();

		Cart activeCart = customerService.findActiveCartForCustomer(customer.getId());

		if (activeCart == null) throw new CartNotFoundException();

		List<Store> stores =
				storeService.findStoresByCityId(customer.getContact().getAddress().getCity().getId());
		List<StoreItem> storeItems = new ArrayList<>();
		StoreItemByDistributorComparator comparator = new StoreItemByDistributorComparator();

		for (Store store : stores) {
			storeItems.addAll(store.getProducts()
					.stream().sorted((i1, i2) -> comparator.compare(i1, i2)).collect(Collectors.toList()));
		}

		model.addAttribute("customer", customer);
		model.addAttribute("cart", activeCart);
		model.addAttribute("storeItems", storeItems);
		model.addAttribute("showSearch", true);

		return "customer/all_products";
	}

	@GetMapping("/cart/{cartId}/item/{itemId}/quantity")
	@ResponseBody
	public ResponseEntity<Integer> getCartItemQuantity(@PathVariable("cartId") Long cartId,
													   @PathVariable("itemId") Long itemId)
			throws CartNotFoundException {
		Cart activeCart = cartService.findById(cartId);

		if (activeCart == null) throw new CartNotFoundException();

		Optional<CartItem> cartItem =
				activeCart.getItems().stream().filter(i -> i.getItem().getId() == itemId).findFirst();

		if (!cartItem.isPresent()) return new ResponseEntity<>(0, HttpStatus.OK);

		return new ResponseEntity<>(cartItem.get().getQuantity(), HttpStatus.OK);
	}

	@PostMapping(value = "/cart/{cartId}/add_item",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public HttpStatus addCartItem(@RequestBody com.denlex.superoptimum.dto.CartItem item,
								  @PathVariable("cartId") Long cartId, Model model) {
		Cart activeCart = cartService.findById(cartId);
		Long storeItemId = Long.valueOf(item.getStoreItemId());
		int quantity = Integer.valueOf(item.getQuantity());
		StoreItem storeItem = storeItemService.findById(storeItemId);

		if (activeCart.getItems().isEmpty()) {
			activeCart.addItems(new CartItem(storeItem, quantity));
		} else {
			Optional<CartItem> result = activeCart.getItems()
					.stream().filter(cartItem -> cartItem.getItem().getId() == storeItemId).findFirst();

			if (!result.isPresent()) {
				activeCart.addItems(new CartItem(storeItem, quantity));
			} else {
				CartItem cartItem = result.get();
				cartItem.setQuantity(quantity);
			}
		}

		activeCart = cartService.save(activeCart);
		model.addAttribute("cart", activeCart);
		return HttpStatus.OK;
	}

	@DeleteMapping(value = "/cart/{cartId}/item/{itemId}")
	@ResponseBody
	public HttpStatus deleteProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable(
			"itemId") Long itemId) {
		Cart activeCart = cartService.findById(cartId);
		activeCart.deleteItem(itemId);
		cartService.save(activeCart);

		return HttpStatus.OK;
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
