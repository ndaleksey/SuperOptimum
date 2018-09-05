package com.denlex.superoptimum.dto;

/**
 * Created by Shishkov A.V. on 26.08.18.
 */

public class CartItem {
	private Long storeItemId;
	private double quantity;

	public CartItem() {
	}

	public CartItem(Long storeItemId, double quantity) {
		this.storeItemId = storeItemId;
		this.quantity = quantity;
	}

	public Long getStoreItemId() {
		return storeItemId;
	}

	public void setStoreItemId(Long storeItemId) {
		this.storeItemId = storeItemId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
