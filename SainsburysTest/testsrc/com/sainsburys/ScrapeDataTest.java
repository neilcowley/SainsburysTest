package com.sainsburys;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ScrapeDataTest {
	
	private ScrapeData classUnderTest ; 
	
	@Before
	public void setUp() throws Exception{
		classUnderTest = new ScrapeData();
	}
	
	@Test
	public void testRun() {
		
		System.out.println("Test in process");
		JsonParser parser = new JsonParser();
		JsonObject o ; 
		
		// Get the JSON for testing
		String json = classUnderTest.run(RunExample.SOURCE_URL);
					
		// Test to see if required nodes are present
		JsonParser jsonParser = new JsonParser();		
		JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

		// Check it is a JSON object		
		if(!jsonObject.isJsonObject()) {
			fail("Not a JSON Object");	
		}
			
		// Check for a result and total element
		assertTrue("Does not contain Result element" , jsonObject.has("result"));
		
		// Get the results element and check to see if any products have been added
		JsonElement result = jsonObject.get("result");
		JsonArray ja = result.getAsJsonArray(); 
		// Check if products have been added
		assertTrue("Does not contain any products" , ja.size()!=0); 
		String[] fieldNames = {"title","unit_price","description"};

		// Test for the three values that should be present - title, unit_price and description. Cals are not always present in a product
		for(JsonElement value : ja) {
			// Get the product as a Json object 
			o = value.getAsJsonObject();
			// Loop around the field names check each 
			for(String fldName : fieldNames) {
				// Check product has a  element
				assertTrue("Product does not have a " + fldName , o.has(fldName)); 
				// Check if title is blank
				assertTrue("Product " + fldName + " is blank" , !o.get(fldName).toString().equals(""));	
			}
		}
				
		// Check for a total element  JsonElement total = jsonObject.get("total");
		if (!jsonObject.has("total")){
		   fail("Does not contain Total element");
		} else {
			JsonElement total = jsonObject.get("total");
			o = parser.parse(total.toString()).getAsJsonObject();
			// Check for a gross element
			assertTrue("Product does not have a gross value" , o.has("gross") );
			// check for a VAT element
			assertTrue("Product does not have a VAT value" , o.has("vat") ); 
					
			/*BigDecimal gross = new BigDecimal(o.get("gross").toString());
			System.out.println(gross.toString());

			BigDecimal vat = new BigDecimal(o.get("vat").toString());
			System.out.println(vat.toString());*/

		}
		
		System.out.println("Test completed");
	}
}
