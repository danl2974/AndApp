package com.dl2974.andapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;
import com.dl2974.andapp.MainActivity;
import android.content.ClipData;

import android.provider.MediaStore.Images.Media;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.MediaScannerConnection;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import android.content.Context;
import 	android.widget.ImageView;


public class ShareContent extends Activity {

	InputStream asyncInstr = null;
	ImageView imgView = new ImageView(this);
	
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	 super.onCreate(savedInstanceState);
        
         //Get Location Address
         Intent intent = getIntent();
         String msg_extra = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    	
         Intent shareintent = new Intent(Intent.ACTION_SEND);
         //shareintent.putExtra(MainActivity.EXTRA_MESSAGE, message);
         shareintent.setType("image/png");
         shareintent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
         shareintent.putExtra(Intent.EXTRA_TEXT, msg_extra);
           
         //Uri img = Uri.parse("android.resource://com.dl2974.andapp/drawable-hdpi/ic_launcher.png");
         
         
         getImageData("http://net29.net/images/mtlogo.png");
         
         /*
         Bitmap bm = BitmapFactory.decodeStream(asyncInstr);
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
         byte[] imgByteArray = stream.toByteArray();
         
         try{
         FileOutputStream  fos = openFileOutput("ic_launcher.png", MODE_WORLD_READABLE);
         fos.write(imgByteArray);
         fos.close();
         }
         catch (Exception e) {
          	  e.printStackTrace();
          	}
         
         File sharedImgFile = getFileStreamPath("ic_launcher.png");
         Uri img =  Uri.fromFile(sharedImgFile);
         
            
         shareintent.putExtra(Intent.EXTRA_STREAM, img);
         
      // Always use string resources for UI text.
      // This says something like "Share this photo with"
      String title = getResources().getString(R.string.chooser_title);
      // Create and start the chooser
      Intent chooser = Intent.createChooser(shareintent, title);
      startActivity(chooser);
    	    	*/
        
         
         //ImageView imgView = new ImageView(this);
         //imgView.setImageBitmap(BitmapFactory.decodeStream(asyncInstr));
         
         //TextView textView = new TextView(this);
         //textView.setTextSize(30);
         //textView.setText(String.valueOf( avail ));
         //textView.setPadding(0, 100, 0, 0);
         setContentView(imgView);
    	    	
    	    	
    	    	
    }
    


private void getImageData(String urlstr){
	
	     ConnectivityManager connMgr = (ConnectivityManager) 
	     getSystemService(Context.CONNECTIVITY_SERVICE);
	     NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	     
	     if (networkInfo != null && networkInfo.isConnected()) {
	    	 
	    	new DownloadTask().execute(urlstr);
	    	
	     }
	     
	     else{
	    	 
	     }
 } 





private class DownloadTask extends AsyncTask<String, Void, InputStream> {
    @Override
    protected InputStream doInBackground(String... urls) {
          
        // params comes from the execute() call: params[0] is the url.
    	InputStream in = null;
        try {	
                URL url = new URL(urls[0]);
                URLConnection conn = url.openConnection();
                
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                int response = -1;
                response = httpConn.getResponseCode();
                
                if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                    }
        	
            return in;
            
            
        } catch (IOException e) {
            return in;
        }
    }
   
    @Override
    protected void onPostExecute(InputStream result) {
    	asyncInstr = result;
    	imgView.setImageBitmap(BitmapFactory.decodeStream(result));
   }
}
   
}