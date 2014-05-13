package com.dl2974.andapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FactualArrayAdapter extends ArrayAdapter<HashMap<String,String>> {
  private final Context context;
  private final ArrayList<HashMap<String,String>> results;

  public FactualArrayAdapter(Context context, ArrayList<HashMap<String,String>> results) {
    super(context, R.layout.resultlayout, results);
    this.context = context;
    this.results = results;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.resultlayout, parent, false);
    TextView name = (TextView) rowView.findViewById(R.id.result_name);
    name.setText(results.get(position).get("name"));
    TextView address = (TextView) rowView.findViewById(R.id.result_address);
    address.setText(results.get(position).get("address"));
    TextView locality = (TextView) rowView.findViewById(R.id.result_locality);
    locality.setText(results.get(position).get("locality"));
    //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    
    /*
    // Change the icon for Windows and iPhone
    String s = values[position];
    if (s.startsWith("Windows7") || s.startsWith("iPhone")
        || s.startsWith("Solaris")) {
      imageView.setImageResource(R.drawable.no);
    } else {
      imageView.setImageResource(R.drawable.ok);
    }
    */
    return rowView;
  }
} 
