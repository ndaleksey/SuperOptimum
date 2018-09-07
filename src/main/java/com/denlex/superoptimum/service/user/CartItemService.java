package com.denlex.superoptimum.service.user;

import com.denlex.superoptimum.domain.product.CartItem;

/**
 * Created by Shishkov A.V. on 26.08.18.
 */
public interface CartItemService {
	CartItem findById(Long id);

	CartItem save(CartItem item);
}
