package com.msn.poc.cart.repository;

import org.springframework.data.repository.CrudRepository;

import com.msn.poc.cart.entity.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

}
