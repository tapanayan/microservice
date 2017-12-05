package com.msn.poc.cart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="invoice")
public class Invoice {
	
	private Integer confirmationNumber;
	
	private Payment payment;
	
	private Product product;

	@Id
	@GeneratedValue( strategy= GenerationType.AUTO )
	@Column(name="CONFIRMATION_NUMBER")
	public Integer getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(Integer confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	@OneToOne
	@JoinColumn(name="PAYMENT_ID")
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@OneToOne
	@JoinColumn(name="PRODUCT_ID")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	

}
