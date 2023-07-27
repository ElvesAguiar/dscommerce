package com.elves.dscommerce.tests;

import com.elves.dscommerce.entities.Category;
import com.elves.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct(){
        Category category =CategoryFactory.createCategory();
        Product product=new Product();
        product.setId(1L);
        product.setPrice(20000.0);
        product.setName("PS5");
        product.setImgUrl("ps5.com/img");
        product.getCategories().add(category);
        product.setDescription("ps5 de qualidade das casas bahias, faria cozinha com a gente.");
        return product;
    }

    public static Product createProduct(String name){
        Product product=createProduct();
        product.setName(name);
        return product;
    }

}
