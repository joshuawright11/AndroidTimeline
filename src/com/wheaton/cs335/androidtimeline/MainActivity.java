package com.wheaton.cs335.androidtimeline;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import storage.AndroidDBHelper;
import storage.DBOpenHelper;
import storage.phpDBHelper;
import storage.phpPushHelper;
import model.Atomic;
import model.Category;
import model.Duration;
import model.TLEvent;
import model.Timeline;
import model.Timeline.AxisLabel;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

public class MainActivity extends Activity {
	
	private ListView timelineList;
	
	private ArrayList<Timeline> timelines;
	
	private ArrayList<String> timelineNames;
	
	private StableArrayAdapter adapter;
	
	private AndroidDBHelper database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		database = new AndroidDBHelper(getBaseContext());
		
		timelines = new ArrayList<Timeline>(Arrays.asList(database.getTimelines()));
		timelineNames = new ArrayList<String>();

		for(Timeline t : timelines)
			timelineNames.add(t.getName());
		
		timelineList = (ListView) findViewById(R.id.timelineListView);
		adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, timelineNames);
		timelineList.setAdapter(adapter);
		
		final Activity activity = this;
		timelineList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Intent intent = new Intent(activity, TimelineDisplayActivity.class);
				
//				Date one = Date.valueOf("1993-09-11");
//				Date two = Date.valueOf("1993-09-21");
//				Date three = Date.valueOf("1993-09-12");
//				Date four = Date.valueOf("1993-09-20");
//				
//				TLEvent event1 = new Atomic("one", new Category(""), one, -1, "");
//				TLEvent event2 = new Duration("two", new Category(""), three,four, -1, "");
//				TLEvent event3 = new Atomic("three", new Category(""), two, -1, "");
//				Timeline test1 = new Timeline("Tester", AxisLabel.MONTHS,Color.BLUE,Color.GRAY);
//				test1.addEvent(event1);
//				test1.addEvent(event2);
//				test1.addEvent(event3);
//				timelines = new ArrayList<Timeline>();
//				timelines.add(test1);

				intent.putExtra("TIMELINE", timelines.get(position));
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//test code
		Date one = Date.valueOf("1993-09-11");
		Date two = Date.valueOf("1993-09-21");
		Date three = Date.valueOf("1993-09-12");
		Date four = Date.valueOf("1993-09-20");
		
		TLEvent event1 = new Atomic("one", new Category(""), one, -1, "");
		TLEvent event2 = new Duration("two", new Category(""), three,four, -1, "");
		TLEvent event3 = new Atomic("three", new Category(""), two, -1, "");
		Timeline test1 = new Timeline("Tester", AxisLabel.MONTHS,Color.BLUE,Color.GRAY);
		test1.addEvent(event1);
		test1.addEvent(event2);
		test1.addEvent(event3);
		timelines = new ArrayList<Timeline>();
		timelines.add(test1);
		//end test code
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {
		case R.id.AddEvent:
			Intent addEve = new Intent(this, AddEvent.class);
			addEve.putExtra("timelines", timelines);
			startActivity(addEve);
			break;
		case R.id.AddCategory:
			Intent addCat = new Intent(this, AddCategory.class);
			addCat.putExtra("timelines", timelines);
			startActivity(addCat);
			break;
		}
		
		return true;
	}

}
