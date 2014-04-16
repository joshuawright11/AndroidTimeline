/**
 * 
 */
package com.wheaton.cs335.androidtimeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Timeline;
import android.app.Fragment;
import android.content.Intent;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sanvedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		timelineList = (ListView) rootView.findViewById(R.id.timelineListView);
		
		final ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[]{"one", "two", "three"}));
		
		final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
		
		timelineList.setAdapter(adapter);
		
		timelineList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				
				Intent intent = new Intent(getActivity(), TimelineDisplayActivity.class);

				intent.putExtra("TIMELINE", new Timeline("Sweet"));
				
				startActivity(intent);
				
			}

		});
		
		return rootView;
		
		
	}
	
}
