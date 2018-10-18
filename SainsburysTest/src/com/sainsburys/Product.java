package com.sainsburys;
import java.math.BigDecimal;

public class Product {

	private final String title;
	private final String description;
	private BigDecimal unit_price;
	private int kcal_per_100g;
	
	public Product(ProductBuilder builder) {
		super();
		this.title = builder.getTitle();
		this.description = builder.getDescription();
		this.unit_price = builder.getUnit_price(); 
		this.kcal_per_100g = builder.getKcal_per_100g();
	}
			
/*	public Product(String title, BigDecimal unit_price, int kcal_per_100g, String description) {
		super();
		this.title = title;
		this.description = description;
		this.unit_price = unit_price; 
		this.kcal_per_100g = kcal_per_100g;
	}*/
	
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
		return "Product [ title=" + title + ", unit_price=" + unit_price + ", kcal_per_100g="
				+ kcal_per_100g + ", description=" + description + "]";
	}

}
