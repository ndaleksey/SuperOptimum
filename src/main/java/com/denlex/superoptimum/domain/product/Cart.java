package com.denlex.superoptimum.domain.product;

import com.denlex.superoptimum.domain.BaseEntity;
import com.denlex.superoptimum.domain.user.Customer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Shishkov A.V. on 07.08.18.
 */
@Entity
public class Cart extends BaseEntity {
	@ManyToOne
	private Customer customer;

	@Enumerated
	private CartStatus status;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "cart", orphanRemoval = true)
	private Set<CartItem> items = new HashSet<>();

	public Cart() {
	}

	public Cart(CartStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CartStatus getStatus() {
		return status;
	}

	public void setStatus(CartStatus status) {
		this.status = status;
	}

	public Set<CartItem> getItems() {
		return items;
	}

	public void setItems(Set<CartItem> items) {
		this.items = items;
	}

	public void addItem(CartItem item) {
		item.setCart(this);
		this.items.add(item);
	}

	public void addItems(CartItem... items) {
		for (CartItem item : items) {
			addItem(item);
		}
	}

	public void deleteItem(Long id) {
		Optional<CartItem> result =
				this.getItems().stream().filter(i -> i.getId() == id).findFirst();
		if (result.isPresent()) this.getItems().remove(result.get());
	}
}