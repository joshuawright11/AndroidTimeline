package com.wheaton.cs335.androidtimeline;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

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
import android.graphics.Color;

public class MainActivity extends Activity {


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		ListView timelineList;
			
		timelineList = (ListView) findViewById(R.id.timelineListView);
		
		final ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[]{"one", "two", "three"}));
		
		final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		
		timelineList.setAdapter(adapter);
		
		final Activity activity = this;
		
		timelineList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				
				Intent intent = new Intent(activity, TimelineDisplayActivity.class);
				
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

				intent.putExtra("TIMELINE", test1);
				
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
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {
		case R.id.AddEvent:
			startActivity(new Intent(this, AddEvent.class));
			break;
		case R.id.AddCategory:
			startActivity(new Intent(this, AddCategory.class));
			break;
		}
		
		return true;
	}

}
