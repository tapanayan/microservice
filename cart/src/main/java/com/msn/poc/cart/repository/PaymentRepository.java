package com.msn.poc.cart.repository;

import org.springframework.data.repository.CrudRepository;

import com.msn.poc.cart.entity.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

}
