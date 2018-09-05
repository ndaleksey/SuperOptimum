package com.denlex.superoptimum.service.user;

import com.denlex.superoptimum.domain.product.Cart;

/**
 * Created by Shishkov A.V. on 05.09.18.
 */
public interface CartService {
	Cart findById(Long id);

	Cart save(Cart cart);
}
