/**
 * 
 */
package com.wheaton.cs335.androidtimeline;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Atomic;
import model.Category;
import model.Duration;
import model.TLEvent;
import model.Timeline;
import model.Timeline.AxisLabel;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author josh
 *
 */
public class TimelineSelectionFragment extends Fragment {
	
	ListView timelineList;
	
	public TimelineSelectionFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		timelineList = (ListView) rootView.findViewById(R.id.timelineListView);
		
		final ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[]{"one", "two", "three"}));
		
		final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
		
		timelineList.setAdapter(adapter);
		
		timelineList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				
				Intent intent = new Intent(getActivity(), TimelineDisplayActivity.class);
				
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
		
		return rootView;
		
		
	}
	
}
