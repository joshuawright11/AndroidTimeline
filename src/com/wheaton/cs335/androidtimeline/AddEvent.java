package com.wheaton.cs335.androidtimeline;

import model.Atomic;
import model.Duration;
import model.Category;
import model.TLEvent;
import model.Timeline;
import java.sql.Date;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.Build;

import java.util.ArrayList;

import storage.DBHelper;

public class AddEvent extends Activity {
	
	boolean checkBox;
	
	ArrayList<Category> categories;
	
	ArrayList<Timeline> timelines;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		checkBox = false;
	}
	
	public void okClick(View view) {
		String title = ((EditText) findViewById(R.id.EventTitle)).getText().toString();
		Date firstDate = new Date(((DatePicker) findViewById(R.id.datePicker1)).getCalendarView().getDate());
		Category category = categories.get(((Spinner) findViewById(R.id.categorySelector)).getSelectedItemPosition());
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
		
		//implement this code
		//DBHelper db = null;
		//event.save(db, timelines.get(((Spinner) findViewById(R.id.addEventTimelineSelector)).getSelectedItemPosition()).getName());
		
		Intent intent = new Intent(this, MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
	    startActivity(intent);
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
			return rootView;
		}
	}

}
