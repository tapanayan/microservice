package com.msn.poc.cart.repository;

import java.util.List;
import com.msn.poc.cart.entity.Cart;

public interface CustomCartRepository {
	List<Cart> findByTokenAndType(String session,String type);
	void deleteByProduct(int productId,String type);
	List<Cart> findByUserIdAndType(String userId,String type);
	List<Cart> findByToken(String sessionToken);
	List<Cart> findByUserId(String userId);
}
