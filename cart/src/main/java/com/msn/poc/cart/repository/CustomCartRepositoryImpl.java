package com.msn.poc.cart.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.msn.poc.cart.entity.Cart;

@Repository
@Transactional(readOnly=true)
public class CustomCartRepositoryImpl implements CustomCartRepository {
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<Cart> findByTokenAndType(String session,String type) {
		System.out.println("find by token.session=>"+session+" type=>"+type);
		Query query=entityManager.createQuery("select c from Cart c where c.token = :session AND c.type = :type");
		query.setParameter("session", session);
		query.setParameter("type", type);
		return query.getResultList();
	}

	@Override
	public void deleteByProduct(int productId,String type) {
		Query query=entityManager.createQuery("delete from Cart c where c.product.productId = :productId AND c.type = :type");
		query.setParameter("productId", productId);
		query.setParameter("type", type);
		query.executeUpdate();
	}

	@Override
	public List<Cart> findByUserIdAndType(String userId, String type) {
		Query query=entityManager.createQuery("select c from Cart c where c.userId = :userId AND c.type = :type");
		query.setParameter("userId", userId);
		query.setParameter("type", type);
		return query.getResultList();
	}

	@Override
	public List<Cart> findByToken(String sessionToken) {
		Query query=entityManager.createQuery("select c from Cart c where c.token = :session");
		query.setParameter("session", sessionToken);
		return query.getResultList();
	}

	@Override
	public List<Cart> findByUserId(String userId) {
		Query query=entityManager.createQuery("select c from Cart c where c.userId = :userId");
		query.setParameter("userId", userId);
		return query.getResultList();
	}

}
