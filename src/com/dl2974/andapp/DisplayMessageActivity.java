package com.dl2974.andapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.TextView;

import android.content.SharedPreferences;
import android.content.Context;
import 	java.io.File;
import 	java.io.FileOutputStream;
import 	java.io.FileInputStream;
import java.io.InputStream;

import com.dl2974.andapp.FeedReaderDbHelper;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import com.dl2974.andapp.FeedReaderContract.FeedEntry;
import com.dl2974.andapp.FeedReaderContract;

import android.content.ContentValues;

import android.database.Cursor;


public class DisplayMessageActivity extends Activity {


	
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


    	super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Date timeNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd ' T ' hh:mm:ss a zzz");
        String datestamp = ft.format(timeNow);
        
        /*
        //Save to SharedPreference KV file
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        
        
        if ( sharedPref.getString("ds","None") != "None" ){
           
        	String dsValue = sharedPref.getString("ds","None"); 
        	message = "Last timestamp registered: " + dsValue + "//" + message;
        	
        }	
        else{
            editor.putString("ds", datestamp);
            editor.commit();
        }
        
        
        //Save to File
        //File file = new File(getFilesDir(), "dltestfile2");
        FileOutputStream outputStream;
        String filestr = "Tester by DL: " + datestamp;
        try {
        	  outputStream = openFileOutput("dltestfile2", Context.MODE_PRIVATE);
        	  outputStream.write(filestr.getBytes());
        	  outputStream.close();
        	} catch (Exception e) {
        	  e.printStackTrace();
        	}
        
        
       
        String fromfile = "";
        File file = new File(getFilesDir(), "dltestfile");
        try{
        FileInputStream fis = new FileInputStream(file);
        StringBuffer fileContent = new StringBuffer();
        byte[] buffer = new byte[1024];
        
        while (fis.read(buffer) != -1) {
            fileContent.append(new String(buffer));
        }
        
        fromfile = fileContent.toString();
        
        fis.close();
        }
        catch (Exception e) {
      	  e.printStackTrace();
      	}
        
        */
        
        
        
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
       
        
     // Gets the data repository in write mode
        SQLiteDatabase wdb = mDbHelper.getWritableDatabase();

        String[] parts = datestamp.split("T");
        
        SimpleDateFormat sdfm = new SimpleDateFormat ("hh:mm");
        SimpleDateFormat sdfs = new SimpleDateFormat ("sss");
        String minsec = sdfm.format(timeNow);
        String msec = sdfs.format(timeNow);
        
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, minsec);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, msec);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = wdb.insert(
                 FeedEntry.TABLE_NAME,
                 FeedEntry.COLUMN_NAME_NULLABLE,
                 values);
        
        
      
     //FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        
     SQLiteDatabase db = mDbHelper.getReadableDatabase();

     // Define a projection that specifies which columns from the database
     // you will actually use after this query.
     String[] projection = {
         FeedEntry._ID,
         FeedEntry.COLUMN_NAME_TITLE,
         FeedEntry.COLUMN_NAME_SUBTITLE
         };

     String selection = "";
     String[] selectionArgs = {};
     // How you want the results sorted in the resulting Cursor
     String sortOrder =
         FeedEntry.COLUMN_NAME_TITLE + " DESC";

     Cursor c = db.query(
         FeedEntry.TABLE_NAME,  // The table to query
         projection,                               // The columns to return
         selection,                                // The columns for the WHERE clause
         selectionArgs,                            // The values for the WHERE clause
         null,                                     // don't group the rows
         null,                                     // don't filter by row groups
         sortOrder                                 // The sort order
         );
     
         String qres = "";
     
         c.moveToFirst();
         do{
         qres += c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_TITLE)) + " | " + c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_SUBTITLE));
         qres += "\n\r";
         }while( c.moveToNext() );
         
         
       
       /* 
       SQLiteDatabase db = mDbHelper.getReadableDatabase();

      // New value for one column
      ContentValues values = new ContentValues();
      values.put(FeedEntry.COLUMN_NAME_SUBTITLE, "test");

      // Which row to update, based on the ID
      String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
      String[] selectionArgs = { "07%" };

      int count = db.update(
          FeedEntry.TABLE_NAME,
          values,
          selection,
          selectionArgs);
      */
         
        
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(10);
        textView.setText(qres + " //MESSAGE: " + message + "... DATESTAMP: " + datestamp);
        //textView.setText(fromfile + " // " + message);

        // Set the text view as the activity layout
        setContentView(textView);
        
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    	
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}