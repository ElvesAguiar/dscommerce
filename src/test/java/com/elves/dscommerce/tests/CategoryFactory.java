package com.elves.dscommerce.tests;

import com.elves.dscommerce.entities.Category;

public class CategoryFactory {
	
	public static Category createCategory() {
		return new Category(1L, "Games");
	}
	
	public static Category createCategory(Long id, String name) {
		return new Category(id, name);
	}
}
