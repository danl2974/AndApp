package com.dl2974.andapp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class FactualClient {
	
	private String key;
	private String secret;
	private String nonce;
	private String timestamp;
	private String endpoint = "http://api.v3.factual.com/t/places";
	private static final int RESTAURANT_CAT = 347;
	private static final SecureRandom RANDOM = new SecureRandom();
	
	public FactualClient(String key, String secret){
		this.key = key;
		this.secret = secret;
		this.nonce = computeNonce();
		this.timestamp = computeTimestamp();
	}

	
	//public ArrayList<HashMap<String,Object>> getRestaurants(double longitude, double latitude, int meters){
	public String getRestaurants(double latitude, double longitude, int meters){
		
		String requestParams = String.format("filters={$and:[{category_ids:%d}]}&geo={$circle:{$center:[%f,%f],$meters:%d}}", RESTAURANT_CAT, latitude, longitude, meters);
		String normalizedParams = requestParams + String.format("&oauth_consumer_key=%s&oauth_nonce=%s&oauth_signature_method=HMAC-SHA1&oauth_timestamp=%s&oauth_version=1.0", this.key, this.nonce, this.timestamp);
		String json = doConnection(normalizedParams);
		return json;
	}
	
	
	private String doConnection(String requestpath){

		String factual = "";
		HttpURLConnection conn = null;
		try{
	     //URL url = new URL(this.endpoint + "?" + requestpath);
	     URL url = new URL("http://net29.net");
	     //Log.i("FactualClient", this.endpoint + "?" + requestpath);
         conn = (HttpURLConnection) url.openConnection();
         conn.setReadTimeout(10000);
         conn.setConnectTimeout(15000);
         conn.setRequestMethod("GET");
         conn.setDoInput(true);
         /*
         conn.setRequestProperty("Authorization", createAuthHeader("GET&" + "&"+URLEncoder.encode(this.endpoint, "UTF-8") + "&"+URLEncoder.encode(requestpath, "UTF-8") ));
         conn.setRequestProperty("Host", "api.v3.factual.com");
         conn.setRequestProperty("X-Target-URI", "http://api.v3.factual.com");
         conn.setRequestProperty("Connection", "Keep-Alive");
         */
         conn.connect();
        
         //int response = conn.getResponseCode();
         //Log.i("FactualClient", "The response is: " + String.valueOf(response));
         InputStream is = conn.getInputStream();
         Log.i("FactualClient", "Input Stream bytes available: " + String.valueOf(is.available()) );
         BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
         StringBuilder sb =  new StringBuilder();
         String sLine = "";
         int i;
         char c;

     	 while ((sLine = reader.readLine()) != null) {
     		sb.append(sLine);
     	 }
     	 /*
         while((i = reader.read()) != -1){
        	   c = (char) i;
	    	   sb.append(c);
	    	   Log.i("FactualClient", sb.toString());
             }
         */
         factual = sb.toString();
		}
		catch(Exception ex){
			Log.e("FactualClientException", ""+ex.getMessage());
		}
		finally {
		    conn.disconnect();
		   }
		
		return factual;
  
   } 
	
	
	private String createAuthHeader(String baseString){
		
		StringBuilder auth = new StringBuilder("OAuth ");
		auth.append("oauth_version=\"1.0\",");
		auth.append( String.format("oauth_consumer_key=\"%s\",", this.key) );
		auth.append( String.format("oauth_timestamp=\"%s\",", this.timestamp) );
		auth.append( String.format("oauth_nonce=\"%s\",", this.nonce) );
		auth.append("oauth_signature_method=\"HMAC-SHA1\",");
		auth.append( String.format("oauth_signature=\"%s\"", computeSignature(baseString)) );
		
		return auth.toString();
	}
	
	
	private String computeSignature(String signatureBaseString){
		
		String signature = "";
		try{
	      SecretKey secretKey = new SecretKeySpec(this.secret.getBytes("UTF-8"), "HmacSHA1");
	      Mac mac = Mac.getInstance("HmacSHA1");
	      mac.init(secretKey);
	      signature = Base64.encodeToString(mac.doFinal(signatureBaseString.getBytes("UTF-8")), Base64.DEFAULT);
		}
		catch(Exception e){Log.d("FactualClientException", e.getMessage());}
		
		return signature;
	}
	
	
	public String computeNonce() {
		    return Long.toHexString(Math.abs(RANDOM.nextLong()));
		  }


	public String computeTimestamp() {
		    return Long.toString(System.currentTimeMillis() / 1000);
		  }
	
	
	
}
