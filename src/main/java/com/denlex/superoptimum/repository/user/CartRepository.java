package com.denlex.superoptimum.repository.user;

import com.denlex.superoptimum.domain.product.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Shishkov A.V. on 05.09.18.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
