package com.sainsburys;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class JsonBuilder {

	public JsonBuilder() {}
	
	/**
	 * Converts the passed in list of products, gross and VAT into the required JSON output
	 * @param productList
	 * @param gross
	 * @param vat
	 * @return
	 */
	public static String convert(List<Product> productList) {
		
		BigDecimal gross = new BigDecimal("0"); 
		BigDecimal vat = new BigDecimal("0"); 
		
		// Create a new JsonArray to store all the products in
		JsonArray datasets = new JsonArray();
		// Loop through the productList creating a dataSet for each product before adding it to the dataSets
        for(Product product : productList) { 
        	
        	JsonObject dataset = new JsonObject();
  	        dataset.addProperty("title", product.getTitle());
  	        if(product.getKcal_per_100g() > 0) {
  	        	dataset.addProperty("kcal_per_100g", product.getKcal_per_100g());
  	        }
  	        dataset.addProperty("unit_price", product.getUnit_price());
  	        dataset.addProperty("description", product.getDescription());
  	        datasets.add(dataset);
  	        // Calculate the running total for the gross value
  	        gross = gross.add(product.getUnit_price());	
        }
        
		// Calculate the VAT on the total gross amount 
		vat = gross.multiply(new BigDecimal("0.2")).setScale(2, RoundingMode.CEILING);
      
        // Create the results object to store products and totals
        JsonObject results = new JsonObject();
        // add all of the products to it
        results.add("result", datasets);
       
        // generate the gross and vat elements
        JsonObject dataset = new JsonObject();
        dataset.addProperty("gross",gross); 
        dataset.addProperty("vat",vat); 
        results.add("total", dataset);
        
        // Generate the JSON Object and return as string
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        return gson.toJson(results);		
	}
	
}
