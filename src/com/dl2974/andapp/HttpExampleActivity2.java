package com.dl2974.andapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class HttpExampleActivity2 extends Activity {
    private static final String DEBUG_TAG = "HttpExample";
    private EditText urlText;
    private TextView textView;
    private ImageView imgView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_test2);   
        urlText = (EditText) findViewById(R.id.myUrl);
        //textView = (TextView) findViewById(R.id.myText);
        imgView = (ImageView) findViewById(R.id.myImg);
    }

    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(View view) {
        // Gets the URL from the UI's text field.
        String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            textView.setText("No network connection available.");
        }
    }

     // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
     // URL string and uses it to create an HttpUrlConnection. Once the connection
     // has been established, the AsyncTask downloads the contents of the webpage as
     // an InputStream. Finally, the InputStream is converted into a string, which is
     // displayed in the UI by the AsyncTask's onPostExecute method.
     public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
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
        	imgView.setImageURI(uriToImage);
        	
        	
        	
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
          int response = conn.getResponseCode();
          Log.d(DEBUG_TAG, "The response is: " + response);
          is = conn.getInputStream();
          Log.i(DEBUG_TAG, "Input Stream bytes available: " + is.available() );

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
  
  
//Reads an InputStream and converts it to a String.
public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
   Reader reader = null;
   reader = new InputStreamReader(stream, "UTF-8");        
   char[] buffer = new char[len];
   reader.read(buffer);
   return new String(buffer);
}

public Bitmap convertBitMap(InputStream stream){
	
	return BitmapFactory.decodeStream(stream);
}
     
}