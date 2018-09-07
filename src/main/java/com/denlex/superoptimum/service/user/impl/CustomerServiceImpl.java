package com.denlex.superoptimum.service.user.impl;

import com.denlex.superoptimum.domain.product.Cart;
import com.denlex.superoptimum.domain.product.CartStatus;
import com.denlex.superoptimum.domain.user.Customer;
import com.denlex.superoptimum.repository.user.CustomerRepository;
import com.denlex.superoptimum.service.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Shishkov A.V. on 08.08.18.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public Customer findById(Long id) {
		Optional<Customer> result = customerRepository.findById(id);

		return result.isPresent() ? result.get() : null;
	}

	@Override
	public Customer findByUsername(String username) {
		return customerRepository.findByUsername(username);
	}

	@Override
	public Cart findActiveCartForCustomer(Long customerId) {
		Customer customer = this.findById(customerId);
		Set<Cart> carts = customer.getCarts();

		if (carts.isEmpty()) {
			return createActiveCartForCustomer(customer);
		}

		Optional<Cart> activeCart = carts.stream().filter(cart -> cart.getStatus() == CartStatus.ACTIVE).findFirst();

		if (!activeCart.isPresent()) {
			return createActiveCartForCustomer(customer);
		}

		return activeCart.get();
	}

	private Cart createActiveCartForCustomer(Customer customer) {
		Cart cart = new Cart(CartStatus.ACTIVE);
		customer.addCart(cart);
		Customer customerFromDb = customerRepository.save(customer);
		Optional<Cart> activeCart = customerFromDb.getCarts()
				.stream().filter(c -> c.getStatus() == CartStatus.ACTIVE).findFirst();
		return activeCart.get();
	}

}
