package com.dl2974.andapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.view.MenuItem;
import java.lang.CharSequence;
import android.widget.TextView;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import android.database.Cursor;



public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.dl2974.andapp.MESSAGE";
	static final int PICK_CONTACT_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_v2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	            doRefresh();
	            return true;
	        case R.id.action_settings:
	            //openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == PICK_CONTACT_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	Uri contactUri = data.getData();
	            // We only need the NUMBER column, because there will be only one row in the result
	            String[] projection = {Phone.NUMBER};

	            // Perform the query on the contact to get the NUMBER column
	            // We don't need a selection or sort order (there's only one result for the given URI)
	            // CAUTION: The query() method should be called from a separate thread to avoid blocking
	            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
	            // Consider using CursorLoader to perform the query.
	            Cursor cursor = getContentResolver()
	                    .query(contactUri, projection, null, null, null);
	            cursor.moveToFirst();

	            // Retrieve the phone number from the NUMBER column
	            int column = cursor.getColumnIndex(Phone.NUMBER);
	            String number = cursor.getString(column);
	            TextView textView = new TextView(this);
	            textView.setTextSize(30);
	            textView.setText(number);
	            textView.setPadding(0, 100, 0, 0);
	            setContentView(textView);
	            
	            
	        }
	    }
	}	
	
	
	public void doRefresh() {
	    
		EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.setText("Refreshed", TextView.BufferType.EDITABLE);
		
	}	
	
	public void selectContact(View view) {
	    
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
	    pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
	    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
		
	}	
	
	
	public void sendMessage(View view) {
	    
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
		
		
	}
	
	
	public void httpConnTest(View view) {
		Intent intent = new Intent(this, HttpExampleActivity2.class);
		startActivity(intent);
		
		
	}
	
	public void getMap(View view) {
	    
		Intent intent = new Intent(this, GetLocationMap.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String mapaddress = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, mapaddress);
		startActivity(intent);
		
	}
	
	public void visitWeb(View view) {
	    
		Intent intent = new Intent(this, VisitWebUrl.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String urlAddress = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, urlAddress);
		startActivity(intent);
		
	}	
	
	public void shareIt(View view) {
	    
		Intent intent = new Intent(this, Share.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String urlAddress = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, urlAddress);
		startActivity(intent);
		
	}	
	
	
}
