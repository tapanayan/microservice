package com.msn.poc.cart.repository;

import org.springframework.data.repository.CrudRepository;

import com.msn.poc.cart.entity.Seller;

public interface SellerRepository extends CrudRepository<Seller, String> {

}
