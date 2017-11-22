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
	public List<Cart> findByToken(String session) {
		Query query=entityManager.createQuery("select c from Cart c where c.token = :session");
		query.setParameter("session", session);
		return query.getResultList();
	}

}
