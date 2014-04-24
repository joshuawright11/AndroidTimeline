package com.wheaton.cs335.androidtimeline;

import model.Atomic;
import model.Duration;
import model.Category;
import model.TLEvent;
import model.Timeline;

import java.io.IOException;
import java.sql.Date;
import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import storage.phpPushHelper;

/**
 * AddEvent is an Activity that allows the user to add events to preexisting timelines.
 * The screen presents a smooth work flow for the user to follow. The user can give
 * the event a name and select whether the event is atomic or duration. A corresponding
 * datePicker is either made visible or invisible based on this selection. The user may
 * then select the timeline to which to add the event. The categories available for
 * selection is then populated per the selected timeline. The user may then click the
 * OK button to add the event to the timeline.
 */
public class AddEvent extends Activity {
	
	/**
	 * This a variable to stand as a reference to this activity.
	 */
	private static Activity thisActivity;
	
	/**
	 * This is a boolean check for the state of the checkBox.
	 * True = Duration Event, False = Atomic Event
	 */
	private boolean checkBox;
	
	/**
	 * This is the array of timelines from MainActivity.
	 */
	private static ArrayList<Timeline> timelines;
	
	/**
	 * This is the ArrayAdapter for the tSpinner.
	 */
	private static ArrayAdapter<String> timeSelector;
	
	/**
	 * This is the Spinner for selecting a timeline.
	 */
	private static Spinner tSpinner;
	
	/**
	 * This is the ArrayAdapter for the cSpinner.
	 */
	private static ArrayAdapter<String> catSelector;
	
	/**
	 * This is the SPinner for selecting a Category.
	 */
	private static Spinner cSpinner;

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * This is the method called upon creation of this Activity.
	 */
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
	
	/**
	 * This method is called when a button is clicked in this Activity. In particular, this means
	 * that when the OK button is selected, an event is constructed, atomic or duration based on the
	 * state of the "checkBox", the category being retrieved from the Category Spinner, and the event
	 * is added to the Timeline. Subsequently, the timelines are saved to the database and finish() is called.
	 * 
	 * @param view
	 */
	public void okClick(View view) {
		Timeline t = timelines.get(tSpinner.getSelectedItemPosition());
		String title = ((EditText) findViewById(R.id.EventTitle)).getText().toString();
		Date firstDate = new Date(((DatePicker) findViewById(R.id.datePicker1)).getCalendarView().getDate());
		Category category = t.getCategory((String)cSpinner.getSelectedItem());
		int iconIndex = 1;
		String description = ((EditText) findViewById(R.id.eventDetails)).getText().toString();
		
		TLEvent event;
		
		if(!checkBox){
			event = new Atomic(title,category,firstDate,iconIndex,description);
		}else{
			Date secondDate = new Date(((DatePicker) findViewById(R.id.datePicker2)).getCalendarView().getDate());
			event = new Duration(title,category,firstDate,secondDate,iconIndex,description);
		}
		
		t.addEvent(event);
		
	    new AsyncTask<Integer, Void, Void>(){
	        @Override
	        protected Void doInBackground(Integer... params) {
	            // **Code**
	        	try {
	        		phpPushHelper.send(timelines);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
		            return null;
		        }
	    }.execute(1, 2, 3, 4, 5);
		
		finish();
	}
	
	/**
	 * This is the method called when the CheckBox is checked, changing the boolean "checkbox"
	 * and making visible or invisible the second date and second date picker.
	 * @param view
	 */
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
	/**
	 * This method populates the menu.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_event, menu);
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		/**
		 * Constructor
		 */
		public PlaceholderFragment() {
		}

		@Override
		/**
		 * This method adds the Fragment to the Activity.
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_event,
					container, false);
			ArrayList<String> timeNames = new ArrayList<String>();
			for(Timeline t : timelines)
				timeNames.add(t.getName());
			
			tSpinner = (Spinner) rootView.findViewById(R.id.addEventTimelineSelector);
			timeSelector = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, timeNames);
			tSpinner.setAdapter(timeSelector);
			
			ArrayList<String> catNames = new ArrayList<String>();
			for(Iterator<Category> it = timelines.get(0).getCategoryIterator(); it.hasNext();){
				catNames.add(it.next().getName());
			}
			
			cSpinner = (Spinner) rootView.findViewById(R.id.categorySelector);
			catSelector = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, catNames);
			cSpinner.setAdapter(catSelector);
			
			tSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				/**
				 * This is a listener for when a new Timeline is selected. This populates
				 * the category spinner with the updated categories.
				 */
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
				public void onNothingSelected(AdapterView<?> parent) {}
		    });
			return rootView;
		}
	}
}
