package com.dl2974.andapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import android.util.Log;

public class FactualQueryParser {
	
	 private static final String LOG_TAG = "FactualQueryParser";

	public static ArrayList<HashMap<String,String>> parseJsonResponse(String response){
		
		ArrayList<HashMap<String,String>> hmlist = new ArrayList<HashMap<String,String>>();
		JSONParser parser= new JSONParser();
		JSONObject obj = null;
        if(response != null && !response.isEmpty()){
		  try {
			obj = (JSONObject) parser.parse(response);
		  } catch (ParseException e) {
			Log.e(LOG_TAG, e.getMessage());
		  }
        
		  JSONObject responseObj = (JSONObject) obj.get("response");
		  //JSONObject dataObj = (JSONObject) responseObj.get("data");
		  //Set<String> keys = dataObj.keySet();
		  JSONArray dataArray = (JSONArray) responseObj.get("data");
		  Iterator i = dataArray.iterator();

		  while (i.hasNext()) {
			  HashMap<String,String> hm = new HashMap<String,String>();
			  JSONObject dataitem = (JSONObject) i.next();
			  for(int j = 0; j < FactualLocation.StringFields.length; j++){
				if (dataitem.get(FactualLocation.StringFields[j]) != null){ 
				  Log.i("Parser",  FactualLocation.StringFields[j] + (String) dataitem.get(FactualLocation.StringFields[j]) );
			      hm.put(FactualLocation.StringFields[j], (String) dataitem.get(FactualLocation.StringFields[j]));
				}else{Log.i("NullParser",  FactualLocation.StringFields[j]);}
			  }
			  hmlist.add(hm);
		  }
        
        }
		return hmlist;
	}

}
