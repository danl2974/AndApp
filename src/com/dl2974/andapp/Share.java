package com.dl2974.andapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dl2974.andapp.HttpExampleActivity2.DownloadWebpageTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class Share extends Activity {

	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             Uri uriToImage = null;
             
             Intent intent = getIntent();
             String stringUrl = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
             
             /*
             try {
            	 FileOutputStream fos = openFileOutput("background0809.png", MODE_WORLD_READABLE);
            	 
            	 InputStream imageInSt = getResources().openRawResource(R.drawable.background0809);
            	 
            	int read = 0; 
         		byte[] bytes = new byte[1024];
         		
        		while ((read = imageInSt.read(bytes)) != -1) {
        			fos.write(bytes, 0, read);
        		}
            	 
            	 
            	 File png = getFileStreamPath("background0809.png");
            	 uriToImage = Uri.fromFile(png);
            	 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             catch(IOException e){
            	 e.printStackTrace();
             }
             

             Intent shareIntent = new Intent();
             shareIntent.setAction(Intent.ACTION_SEND);
             shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
             shareIntent.setType("image/png");
             startActivity(Intent.createChooser(shareIntent, "Send to here"));       
    	     */
             
             new DownloadImage().execute(stringUrl);
             
             
	}
	

	   public class DownloadImage extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... urls) {
	              
	            // params comes from the execute() call: params[0] is the url.
	            try {
	                return downloadUrl(urls[0]);
	            } catch (IOException e) {
	                //return "Unable to retrieve web page. URL may be invalid.";
	                return null;
	            }
	        }
	        // onPostExecute displays the results of the AsyncTask.
	        @Override
	        protected void onPostExecute(String result) {
	            //textView.setText(result);
	        	//imgView.setImageBitmap(result);
	        	File png = getFileStreamPath(result);
	        	Uri uriToImage = Uri.fromFile(png);
	        	addReadyUri(uriToImage);
	        	
	        	
	        	
	       }
	    }

	     
	  // Given a URL, establishes an HttpUrlConnection and retrieves
	  // the web page content as a InputStream, which it returns as
	  // a string.
	  private String downloadUrl(String myurl) throws IOException {
	      InputStream is = null;
	      String[] urlsegs = myurl.split("/");
	      String filename = urlsegs[urlsegs.length - 1];
	      // Only display the first 500 characters of the retrieved
	      // web page content.
	      int len = 500;
	          
	      try {
	          URL url = new URL(myurl);
	          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	          conn.setReadTimeout(10000 /* milliseconds */);
	          conn.setConnectTimeout(15000 /* milliseconds */);
	          conn.setRequestMethod("GET");
	          conn.setDoInput(true);
	          // Starts the query
	          conn.connect();
	          //int response = conn.getResponseCode();

	          is = conn.getInputStream();
	         

	          // Convert the InputStream into a string
	          //String contentAsString = readIt(is, len);
	          //return contentAsString;
	          //return is;
	                   
			FileOutputStream fos = openFileOutput(filename, MODE_WORLD_READABLE);
	        int read = 0; 
	        byte[] bytes = new byte[1024];
		    while ((read = is.read(bytes)) != -1) {
			    fos.write(bytes, 0, read);
					}
				
		    
	      } 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
	      
	      finally {
	          if (is != null) {
	              is.close();
	          } 
	      }
	      
	      return filename;
	  }
	
public void addReadyUri(Uri uriToImage){

    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
    shareIntent.setType("image/png");
    startActivity(Intent.createChooser(shareIntent, "Send to here"));
	  
}

}
