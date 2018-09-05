package com.denlex.superoptimum.service.user.impl;

import com.denlex.superoptimum.domain.product.CartItem;
import com.denlex.superoptimum.repository.user.CartItemRepository;
import com.denlex.superoptimum.service.user.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Shishkov A.V. on 26.08.18.
 */
@Service
public class CartItemServiceImpl implements CartItemService {
	@Autowired
	private CartItemRepository repository;

	@Override
	public CartItem save(CartItem item) {
		return repository.save(item);
	}
}
