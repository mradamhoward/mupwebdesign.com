package com.clothes.models;

public class Product {
	public int id;
	public String name, desc, image;
	public double price;
	public String countryOfOrigin;
	
	public Product(int id, String name, String desc,String image, double price) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.image = image;
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Product() {
		
	}
	
	public Product(int id) {
		this.id = id;
	}
	public Product(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public Product(int id, String name, String desc) {
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
	
	public Product(int id, String name, String desc, String image) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.image = image;
	}
	
	
}
