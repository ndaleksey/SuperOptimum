package com.denlex.superoptimum.util;

import com.denlex.superoptimum.domain.product.StoreItem;

import java.util.Comparator;

/**
 * Created by Shishkov A.V. on 07.09.18.
 */
public class StoreItemByDistributorComparator implements Comparator<StoreItem> {
	@Override
	public int compare(StoreItem item1, StoreItem item2) {

		if (item1 == item2) return 0;

		String name1 = item1.getStore().getDistributor().getCompanyName();
		String name2 = item2.getStore().getDistributor().getCompanyName();
		return name1.compareToIgnoreCase(name2);
	}
}
