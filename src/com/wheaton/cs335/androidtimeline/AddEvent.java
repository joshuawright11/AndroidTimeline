package com.wheaton.cs335.androidtimeline;

import model.Atomic;
import model.Duration;
import model.Category;
import model.TLEvent;
import model.Timeline;

import java.sql.Date;
import java.util.Arrays;
import java.util.Iterator;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.Build;

import java.util.ArrayList;

import storage.DBHelperAPI;

public class AddEvent extends Activity {
	
	private static Activity thisActivity;
	
	boolean checkBox;
	
	static ArrayList<Category> categories;
	
	static ArrayList<Timeline> timelines;
	
	static DBHelperAPI database;
	
	static ArrayAdapter<String> timeSelector;
	
	static ArrayAdapter<String> catSelector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		thisActivity = this;
		
		checkBox = false;
		
		timelines = (ArrayList<Timeline>) getIntent().getSerializableExtra("timelines");
	}
	
	public void okClick(View view) {
		String title = ((EditText) findViewById(R.id.EventTitle)).getText().toString();
		Date firstDate = new Date(((DatePicker) findViewById(R.id.datePicker1)).getCalendarView().getDate());
		//Category category = categories.get(((Spinner) findViewById(R.id.categorySelector)).getSelectedItemPosition());
		Category category = null;
		//iconIndex?
		int iconIndex = 1;
		String description = ((EditText) findViewById(R.id.eventDetails)).getText().toString();
		
		TLEvent event;
		
		if(!checkBox){
			event = new Atomic(title,category,firstDate,iconIndex,description);
		}else{
			Date secondDate = new Date(((DatePicker) findViewById(R.id.datePicker2)).getCalendarView().getDate());
			event = new Duration(title,category,firstDate,secondDate,iconIndex,description);
		}
		
		//uncomment when concrete DBHelper is available
		//event.save(database, timelines.get(((Spinner) findViewById(R.id.addEventTimelineSelector)).getSelectedItemPosition()).getName());
		
		finish();
	}
	
	public void onCheckboxClicked(View view) {
		TextView text = (TextView) findViewById(R.id.secondDate);
		DatePicker picker = (DatePicker) findViewById(R.id.datePicker2);
		if(!checkBox){
			text.setVisibility(View.INVISIBLE);
			picker.setVisibility(View.INVISIBLE);
		}else{
			text.setVisibility(View.VISIBLE);
			picker.setVisibility(View.VISIBLE);
		}
		checkBox = !checkBox;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_event, menu);
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_event,
					container, false);
			ArrayList<String> timeNames = new ArrayList<String>();
			for(Timeline t : timelines)
				timeNames.add(t.getName());
			
			Spinner tSpinner = (Spinner) rootView.findViewById(R.id.addEventTimelineSelector);
			timeSelector = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, timeNames);
			tSpinner.setAdapter(timeSelector);
			
			ArrayList<String> catNames = new ArrayList<String>();
			for(Iterator<Category> it = timelines.get(0).getCategoryIterator(); it.hasNext();){
				catNames.add(it.next().getName());
			}
			
			Spinner cSpinner = (Spinner) rootView.findViewById(R.id.categorySelector);
			catSelector = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, catNames);
			cSpinner.setAdapter(catSelector);
			
			tSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					Timeline t = timelines.get(position);
		        	ArrayList<String> catNames = new ArrayList<String>();
		        	for(Iterator<Category> it = t.getCategoryIterator(); it.hasNext();){
		        		catNames.add(it.next().getName());
		        	}
		        	catSelector = new ArrayAdapter<String> (thisActivity, android.R.layout.simple_list_item_1, catNames);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
		    });
			
			return rootView;
		}
	}

}
