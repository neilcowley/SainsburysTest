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


public class ProductScraper {

	public List<Product> run(String source_url) {

		Document document;
		Document productPage; 
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
				
				// Create a new immutable product pTitle,pUnit_price,pKcal_per_100g,pDesc
				Product product = new ProductBuilder()
						.title(pTitle)
						.description(pDesc)
						.kcal_per_100gStr(pKcal_per_100g)
						.unit_priceStr(pUnit_price)
						.build();
				productList.add(product);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return productList; 
	}
	
	
}
