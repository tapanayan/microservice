package com.msn.poc.cart.repository;

import java.util.List;
import com.msn.poc.cart.entity.Cart;

public interface CustomCartRepository {
	List<Cart> findByToken(String session);
}
