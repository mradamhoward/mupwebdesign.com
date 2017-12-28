package com.clothes.models;

import java.util.ArrayList;
import java.util.Date;



public class Order {
	public int id;
	ArrayList<Product> products = new ArrayList<Product>();
	
	User customer;
	String customerName;
	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public long getVisa() {
		return visa;
	}

	public void setVisa(long visa) {
		this.visa = visa;
	}

	String customerEmail;
	public String prodList = null;
	Date now = new Date();
	String time = now.toString();
	double amount = 0;
	
	ArrayList<Product> thisOrder = new ArrayList<Product>();
	
	Card paymentInfo;
	long visa = 1000;
	
	
	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
		prodList = productIDS();
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ArrayList<Product> getThisOrder() {
		return thisOrder;
	}

	public void setThisOrder(ArrayList<Product> thisOrder) {
		this.thisOrder = thisOrder;
		prodList = productIDS();
	}
	
	
	public Card getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(Card paymentInfo) {
		this.paymentInfo = paymentInfo;
		try {
			visa = paymentInfo.getVisaDebitNumber();
		} catch (Exception e) {
			
		}
		
	}

	public Order() {
		prodList = productIDS();
	}
	
	public void addCart(ArrayList<Product> a) {
		this.thisOrder = a;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public User getCustomer() {
		return customer;
	}
	public void setCustomer(User customer) {
		this.customer = customer;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public void addProduct(Product p) {
		products.add(p);
	}
	
	public void addProduct(String name) {
		Product p = new Product();
		p.setName(name);
		products.add(p);
	}
	public Order(int id, User customer, Product p) {
		addProduct(p);
		this.id = id;
		this.time = new Date().toString();
		this.customer = customer;
	}
	
	public String productIDS() {
		String res = null;
		
		for(Product p: thisOrder) {
			res += ", " + p.getName();
		}
		
		return res;
	}
	
	public String payment() {
		String payment = null;
		
		payment += paymentInfo.getVisaDebitNumber();
		payment += paymentInfo.getCvv();
		payment += paymentInfo.getExpiry();
		
		return payment;
	}

	
}
