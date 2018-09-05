package com.denlex.superoptimum.domain.product;

import com.denlex.superoptimum.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by Shishkov A.V. on 07.08.18.
 */
@Entity
public class CartItem extends BaseEntity {
	@OneToOne
	private StoreItem item;

	@Column
	private int quantity;

	@ManyToOne
	private Cart cart;

	public CartItem() {
	}

	public CartItem(StoreItem item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public CartItem(StoreItem item, int quantity, Cart cart) {
		this.item = item;
		this.quantity = quantity;
		this.cart = cart;
	}

	public StoreItem getItem() {
		return item;
	}

	public void setItem(StoreItem item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
}
