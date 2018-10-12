package com.sainsburys;

import java.math.BigDecimal;

public class Product {

	private final String title;
	private final String unit_priceStr;
	private final String description;
	private BigDecimal unit_price;
	private int kcal_per_100g;
		
	public Product(String title, String unit_priceStr, String kcal_per_100gStr, String description) {
		super();
		this.title = title;
		this.unit_priceStr = unit_priceStr;
		this.description = description;
		
		// Set the actual unit price 
		String price = this.unit_priceStr.split("/")[0].replace("£",""); 
		unit_price = new BigDecimal(price);
		// Get the number of calories
		if(!("").equals(kcal_per_100gStr))
			this.kcal_per_100g = Integer.parseInt(kcal_per_100gStr.replaceAll("[^\\d.]", ""));
	}
	
	public int getKcal_per_100g() {
		return kcal_per_100g;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public BigDecimal getUnit_price() {
		return unit_price;
	}
	
	@Override
	public String toString() {
		return "Product [ title=" + title + ", unit_priceStr=" + unit_priceStr + ", kcal_per_100g="
				+ kcal_per_100g + ", description=" + description + "]";
	}

}
