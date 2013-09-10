package com.dl2974.andapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;


public class VisitWebUrl extends Activity {

	
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	 super.onCreate(savedInstanceState);
        
         //Get Location Address
         Intent intent = getIntent();
         String urlAddress = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    	
        // Build the intent
   	    Uri webpage = Uri.parse( String.format("http://%s", urlAddress) );
   	    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
  
        // Start an activity if it's safe
        if (isIntentSafe) {
        	
            startActivity(webIntent);
        	
         }
    	    	
    }

   
}