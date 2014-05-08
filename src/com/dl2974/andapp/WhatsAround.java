package com.dl2974.andapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WhatsAround extends Activity {

	
	    @SuppressLint("NewApi")
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        FactualClient fc = new FactualClient("iaDJkLcnsCp7uvSe025F7vYR39eTzAk7uMFalBaq", "gkvlBEWzFWYJBnE1I9ZfAkcQWkAaAmCGWVGU7ojo");
	        String factualresp = fc.getRestaurants(26.303359, -80.122905, 10000);
	        TextView textView = new TextView(this);
	        textView.setTextSize(10);
	        textView.setText(factualresp);

	        setContentView(textView);
	    	
	    }
	
	
}
