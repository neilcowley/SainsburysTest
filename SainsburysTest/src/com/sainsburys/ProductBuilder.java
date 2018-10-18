package com.sainsburys;

import java.math.BigDecimal;

public class ProductBuilder {
	
	private String title="";
	private String unit_priceStr;
	private String description;
	private String kcal_per_100gStr;
	BigDecimal unit_price;
	int kcal_per_100g;
	
	// Setters
	public ProductBuilder title(String title) {
		this.title = title;
		return this;
	}
	public ProductBuilder unit_priceStr(String unit_priceStr) {
		this.unit_priceStr = unit_priceStr;
		return this;
	}
	public ProductBuilder description(String description) {
		this.description = description;
		return this;
	}
	public ProductBuilder kcal_per_100gStr(String kcal_per_100gStr) {
		this.kcal_per_100gStr = kcal_per_100gStr;
		return this;
	}
	
	// Getters
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public BigDecimal getUnit_price() {
		return unit_price;
	}
	public int getKcal_per_100g() {
		return kcal_per_100g;
	}
	
	public Product build(){
		
		if(title==null){
			throw new IllegalStateException("No Title");
		}
		if(unit_priceStr==null){
			throw new IllegalStateException("No Unit Price");
		}else {
			// Set the actual unit price as a bigDecimal 
			String price = this.unit_priceStr.split("/")[0].replace("£","");
			unit_price = new BigDecimal(price);
		}
		if(description==null){
			throw new IllegalStateException("No Description");
		}
		
		// Get the number of calories if present		
		if(!("").equals(kcal_per_100gStr))
			kcal_per_100g = Integer.parseInt(kcal_per_100gStr.replaceAll("[^\\d.]", ""));
		
		return new Product(this);
	}

}
