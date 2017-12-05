package com.msn.poc.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.msn.poc.cart.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>,CustomProductRepository {

}
