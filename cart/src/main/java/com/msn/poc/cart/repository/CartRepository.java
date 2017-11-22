package com.msn.poc.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.msn.poc.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>,CustomCartRepository {

}
