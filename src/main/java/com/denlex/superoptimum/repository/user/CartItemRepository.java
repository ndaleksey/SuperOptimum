package com.denlex.superoptimum.repository.user;

import com.denlex.superoptimum.domain.product.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Shishkov A.V. on 26.08.18.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
