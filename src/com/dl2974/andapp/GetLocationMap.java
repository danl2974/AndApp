package com.dl2974.andapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;


public class GetLocationMap extends Activity {

	
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        
       //Get Location Address
       Intent intent = getIntent();
       String mapAddress = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    	
       // Build the intent
       //Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
       Uri location = Uri.parse( String.format("geo:0,0?q=%s", mapAddress.replaceAll(" ","+") ) );
       Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
  
        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(mapIntent);
         }
    	
    	/*
    	Uri webpage = Uri.parse("http://www.android.com");
    	Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
    	startActivity(webIntent);
    	*/
    	
    }

   
}