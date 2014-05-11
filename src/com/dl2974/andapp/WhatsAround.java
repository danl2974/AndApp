package com.dl2974.andapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class WhatsAround extends Activity {


    private static final String DEBUG_TAG = "WhatsAround";
    private EditText urlText;
    private TextView textView;
    private ImageView imgView;


    private String key;
    private String secret;
    private String nonce;
    private String timestamp;
    private String endpoint = "http://api.v3.factual.com/t/places";
    private static final int RESTAURANT_CAT = 347;
    private static final SecureRandom RANDOM = new SecureRandom();

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_test);   
        urlText = (EditText) findViewById(R.id.myUrl);
        textView = (TextView) findViewById(R.id.myText);

    }


    public void myClickHandler(View view) {
        
        String categoryId = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

        this.key = "iaDJkLcnsCp7uvSe025F7vYR39eTzAk7uMFalBaq";
	    this.secret = "gkvlBEWzFWYJBnE1I9ZfAkcQWkAaAmCGWVGU7ojo";
	    this.nonce = computeNonce();
	    this.timestamp = computeTimestamp();
            new FactualClientTask().execute(categoryId);

        } else {
            textView.setText("No network connection available.");
        }
    }


     public class FactualClientTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
              
            try {
                return callFactual(urls[0]);
            } catch (IOException e) {
                
                return null;
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
               
             textView.setText(result);
        	//imgView.setImageBitmap(result);
        	//File png = getFileStreamPath(result);
        	//Uri uriToImage = Uri.fromFile(png);
        	//Log.i(DEBUG_TAG, "Uri Image: " + String.valueOf(uriToImage) );
        	//imgView.setImageURI(uriToImage);
        	
        	
        	
       }
    }

     

  private String callFactual(String categoryId) throws IOException {

     double latitude = 26.303359;
     double longitude = -80.122905;
     int meters = 10000;
     String requestParams = String.format("filters={\"$and\":[{\"category_ids\":%d}]}&geo={\"$circle\":{\"$center\":[%f,%f],\"$meters\":%d}}", Integer.valueOf(categoryId), latitude, longitude, meters);
     String normalizedParams = requestParams + String.format("&oauth_consumer_key=%s&oauth_nonce=%s&oauth_signature_method=HMAC-SHA1&oauth_timestamp=%s&oauth_version=1.0", this.key, this.nonce, this.timestamp);
     String json = doConnection(normalizedParams, requestParams);
     //String json = doConnection(requestParams);
     return json;

  }
  



		private String doConnection(String requestpath, String requestParams){

			String factual = "";
			HttpURLConnection conn = null;
			InputStream is = null;
			try{
			 String urlString = this.endpoint + "?" + requestParams;
			 //String urlString = "http://c.sweepstakes-web.com" + "?" + requestParams;
		     URL url = new URL(urlString);
		     Log.i("FactualClient", urlString);
	         conn = (HttpURLConnection) url.openConnection();
	         //conn.setReadTimeout(10000);
	         //conn.setConnectTimeout(15000);
	         conn.setRequestMethod("GET");
	         conn.setDoInput(true);
	         
	         
	         conn.setRequestProperty("Authorization",  createAuthHeader("GET&" + "&"+URLEncoder.encode(this.endpoint, "UTF-8") + "&"+URLEncoder.encode(requestpath, "UTF-8") ) );
	         //conn.setRequestProperty("Host", "api.v3.factual.com");
	         //conn.setRequestProperty("X-Target-URI", "http://api.v3.factual.com");
	         //conn.setRequestProperty("Connection", "Keep-Alive");
	         
		     conn.connect();
	         int response = conn.getResponseCode();
	         Log.i("FactualClient", "The response is: " + String.valueOf(response));
	         if (response == 200){
	              is = conn.getInputStream();
	         }
	         else{
	        	 is = conn.getErrorStream();
	         }
	         //Log.i("FactualClient", "Input Stream bytes available: " + String.valueOf(is.available()) );
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
	         Log.i("FactualClient",  sb.toString() );
			}
			catch(Exception e){
				Log.e("FactualClientException", "Exception doConnection");
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
			auth.append( String.format("oauth_signature=\"%s", computeSignature(baseString)) );
			
			Log.i("FactualClient", auth.toString());
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