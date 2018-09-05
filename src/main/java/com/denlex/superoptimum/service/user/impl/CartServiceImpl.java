package com.denlex.superoptimum.service.user.impl;

import com.denlex.superoptimum.domain.product.Cart;
import com.denlex.superoptimum.repository.user.CartRepository;
import com.denlex.superoptimum.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Shishkov A.V. on 05.09.18.
 */
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository repository;

	@Override
	public Cart findById(Long id) {
		Optional<Cart> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	@Override
	public Cart save(Cart cart) {
		return repository.save(cart);
	}
}
