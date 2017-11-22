package com.msn.poc.cart.repository;

import org.springframework.data.repository.CrudRepository;

import com.msn.poc.cart.entity.Product;

public interface ProductRepository extends CrudRepository<Product, String> {

}
