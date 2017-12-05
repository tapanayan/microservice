package com.msn.poc.cart.bm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msn.poc.cart.entity.Cart;
import com.msn.poc.cart.entity.Invoice;
import com.msn.poc.cart.entity.Payment;
import com.msn.poc.cart.feignclient.UserFeignClient;
import com.msn.poc.cart.model.CheckoutRequest;
import com.msn.poc.cart.model.Product;
import com.msn.poc.cart.model.ResponseObject;
import com.msn.poc.cart.model.UserDetail;
import com.msn.poc.cart.repository.CartRepository;
import com.msn.poc.cart.repository.InvoiceRepository;
import com.msn.poc.cart.repository.PaymentRepository;
import com.msn.poc.cart.repository.ProductRepository;
import com.msn.poc.cart.repository.SellerRepository;
import com.msn.poc.cart.utility.SMTPUtility;

public class CheckoutBusinessManager extends AbstractBusinessManager {
	private static final String mailFrom="sales@abc.com";
	public ResponseObject checkout(CheckoutRequest checkoutRequest, CartRepository cartRepository,
			ProductRepository productRepository, UserFeignClient userFeignClient, PaymentRepository paymentRepo, InvoiceRepository invoiceRepo,
			RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper,SellerRepository sellerRepository) {
		List<com.msn.poc.cart.entity.Product> products = getAllProductsFromCart(checkoutRequest.getSessionToken() , cartRepository,
				userFeignClient,redisTemplate,objectMapper,sellerRepository,productRepository);
		Double totalAmount=0d;
		Payment payment = new Payment();
		payment.setAccountHolder(checkoutRequest.getNameOnCard());
		payment.setAmount(totalAmount);
		payment.setBankIfsc(checkoutRequest.getBankIfsc());
		payment.setBankName(checkoutRequest.getBankName());
		payment.setCardNumber(checkoutRequest.getCardNumber());
		payment.setCategory(checkoutRequest.getCategory());
		payment.setCurrency(products.get(0).getCurrency());
		payment.setPaymentDate(new Timestamp(new Date().getTime()));
		payment = paymentRepo.save(payment);
		
		List<Invoice> invoices = new ArrayList<Invoice>();
		for(com.msn.poc.cart.entity.Product product:products){
			Invoice invoice=new Invoice();
			invoice.setProduct(product);
			invoice.setPayment(payment);
			
			totalAmount+=product.getPrice();
			
			product.setAlreadySold(true);
			invoices.add(invoice);
		}
		
		Iterable<Invoice> savedInvoices = invoiceRepo.save(invoices);
		productRepository.save(products);
		
		payment.setAmount(totalAmount);
		payment.setInvoices(invoices);
		
		paymentRepo.save(payment);	
		
		sendMail(checkoutRequest.getEmail(),savedInvoices,totalAmount,payment.getCurrency());
		
		ResponseObject responseObject= new ResponseObject();
		responseObject.setStatus("SUCCESSFUL");
		return responseObject;
	}

	private void sendMail(String email, Iterable<Invoice> savedInvoices,Double totalAmount,String currency) {
		String content="Thank you for shopping with us.<br><table><tr><td>Item</td><td>Price</td><td>Confirmation #</td></tr>";
		for(Invoice invoice:savedInvoices){
			content+="<tr>";
			content+="<td>"+invoice.getProduct().getProductName()+"</td>";
			content+="<td>"+invoice.getProduct().getPrice()+" "+invoice.getProduct().getCurrency()+"</td>";
			content+="<td>"+invoice.getConfirmationNumber()+"</td>";
			content+="</tr>";
		}
		content+="<tr><td colspan='2'>Total</td><td>"+totalAmount+" "+currency+"</td></tr></table>";
		SMTPUtility.send(email, mailFrom, "Sales Invoice", content);		
	}

	private List<com.msn.poc.cart.entity.Product> getAllProductsFromCart(String sessionToken , CartRepository cartRepository, 
			UserFeignClient userFeignClient,RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper,
			SellerRepository sellerRepository,ProductRepository productRepository) {
		UserDetail detail=userFeignClient.decodeToken(sessionToken);
		List<com.msn.poc.cart.entity.Product> products=new ArrayList<com.msn.poc.cart.entity.Product>();
		List<Cart> carts ;
		if(NOT_LOGGED_IN_USER.equals(detail.getUserName())){
			//cache implementation
//			carts = cartRepository.findByTokenAndType(sessionToken,TYPE_CART);
			List<Product> productModels=getProductsFromRedis(sessionToken, redisTemplate, objectMapper);
			carts = new ArrayList<>();
			for(Product product:productModels){
				carts.add(getCart(product, sellerRepository, productRepository));
			}			
		}else{
			carts = cartRepository.findByUserIdAndType(detail.getUserName(), TYPE_CART);
		}
		for(Cart cart:carts){
			if (!cart.getProduct().isAlreadySold()) {
				products.add(cart.getProduct());
			}			
		}
		return products;
	}

}
