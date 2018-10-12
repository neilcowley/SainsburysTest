package com.sainsburys;

public class RunExample {
	
	public static final String SOURCE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html"; 
	
	public static void main(String[] args) {
		
		ScrapeData sd = new ScrapeData();
		String json = sd.run(SOURCE_URL);
		System.out.println(json);
	}

}
