package com.msn.poc.cart.repository;

import java.util.List;

import com.msn.poc.cart.entity.Product;

public interface CustomProductRepository {
	public List<Product> findBySellerProductId(String sellerProductId);

}
