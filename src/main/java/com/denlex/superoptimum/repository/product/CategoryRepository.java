package com.denlex.superoptimum.repository.product;

import com.denlex.superoptimum.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Shishkov A.V. on 07.08.18.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);
}
