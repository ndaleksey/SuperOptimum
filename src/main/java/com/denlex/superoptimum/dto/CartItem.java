package com.denlex.superoptimum.dto;

/**
 * Created by Shishkov A.V. on 26.08.18.
 */

public class CartItem {
	private String storeItemId;
	private String quantity;

	public CartItem() {
	}

	public CartItem(String storeItemId, String quantity) {
		this.storeItemId = storeItemId;
		this.quantity = quantity;
	}

	public String getStoreItemId() {
		return storeItemId;
	}

	public void setStoreItemId(String storeItemId) {
		this.storeItemId = storeItemId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}
