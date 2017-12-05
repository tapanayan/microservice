package com.msn.poc.cart.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.msn.poc.cart.entity.Product;

@Repository
@Transactional(readOnly = true)
public class CustomProductRepositoryImpl implements CustomProductRepository{

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<Product> findBySellerProductId(String sellerProductId) {
		Query query = entityManager.createQuery("select p from Product p where p.sellerProductId= :sellerProductId");
		query.setParameter("sellerProductId", sellerProductId);
		return query.getResultList();		
	}

}
