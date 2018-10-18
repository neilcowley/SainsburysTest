package com.sainsburys;

import java.util.List;

public class RunExample {
	
	public static final String SOURCE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html"; 
	
	public static void main(String[] args) {
		
		ProductScraper ps = new ProductScraper();
		List<Product> productList =  ps.run(SOURCE_URL);
		// Return a JSON string
		String json = JsonBuilder.convert(productList);
		System.out.println(json);
	}

}
