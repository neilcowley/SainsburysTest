package com.sainsburys;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ScrapeData {
	
	public String run(String source_url) {
		
		String json = ""; 
		Document document;
		Document productPage; 
		BigDecimal gross = new BigDecimal("0"); 
		BigDecimal vat = new BigDecimal("0"); 
		List<Product> productList = new ArrayList<>();
		
		try {
			//Get Document object after parsing the html from given url.
			document = Jsoup.connect(source_url).get();

			// Scrape all of the item names, urls and prices from the first page
			Elements itemName = document.select(".productNameAndPromotions"); //Get Name of item
			Elements url = document.select(".productNameAndPromotions > h3 > a");
			Elements pricePerUnit = document.select(".pricePerUnit"); //Get unit price
			
			// Loop around the itemName list creating Products for each item 
			for (int i=0; i < itemName.size(); i++) {
				
				String pUrl="" ;
				String pTitle="";
				String pKcal_per_100g=""; 
				String pUnit_price="";
				String pDesc=""; 
				
				// Get the URL, title and unit price
				pUrl = url.get(i).attr("abs:href");
				pTitle = itemName.get(i).text().trim();
				pUnit_price = pricePerUnit.get(i).text();
				
				// Follow the URL to the product information print ("url=" + item.url);
				productPage = Jsoup.connect(pUrl).get();
				// The description can be stored in one of two elements, check for the productText class initially
				Element desc = productPage.selectFirst(".productText > p");
				if(desc == null) {
					// The description is stored in a memo div 
					desc = productPage.selectFirst(".memo > p");	
				}
				pDesc = desc.text();
				// Check to see if the item has a calorie value
				Element cals = productPage.selectFirst(".nutritionLevel1");
				if(cals != null) {
					pKcal_per_100g= cals.text();
				}
				
				// Create a new immutable product
				Product product = new Product(pTitle,pUnit_price,pKcal_per_100g,pDesc);
				productList.add(product);
				// Calculate the gross amount
				gross = gross.add(product.getUnit_price());	
			}
			
			// Calculate the VAT on the total gross amount 
			vat = gross.multiply(new BigDecimal("0.2")).setScale(2, RoundingMode.CEILING);
			// Return a JSON string
			json = convertToJSON(productList, gross, vat);
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	/**
	 * Converts the passed in list of products, gross and VAT into the required JSON output
	 * @param productList
	 * @param gross
	 * @param vat
	 * @return
	 */
	private String convertToJSON(List<Product> productList, BigDecimal gross, BigDecimal vat) {
		
		// Create a new JsonArray to store all the products in
		JsonArray datasets = new JsonArray();
		// Loop through the productList creating a dataSet for each product before adding it to the dataSets
        for(Product prod : productList) {     
        	JsonObject dataset = new JsonObject();
  	        dataset.addProperty("title", prod.getTitle());
  	        if(prod.getKcal_per_100g() > 0) {
  	        	dataset.addProperty("kcal_per_100g", prod.getKcal_per_100g());
  	        }
  	        dataset.addProperty("unit_price", prod.getUnit_price());
  	        dataset.addProperty("description", prod.getDescription());
  	        datasets.add(dataset);
        }
      
        // Create the results object to store products and totals
        JsonObject results = new JsonObject();
        // add all of the products to it
        results.add("result", datasets);
       
        // generate the gross and vat elements
        JsonObject dataset = new JsonObject();
        dataset.addProperty("gross",gross); 
        dataset.addProperty("vat",vat); 
        results.add("total", dataset);
        
        // Generate the JSON Object 
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        return gson.toJson(results);		
	}
}
