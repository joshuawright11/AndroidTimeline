/**
 * 
 */
package com.wheaton.cs335.androidtimeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Fragment;
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
				final String item = (String) parent.getItemAtPosition(position);
				// nifty animation example. Sets the transparency (alpha) to 0 over 2 seconds, then removes from the list
				view.animate().setDuration(2000).alpha(0)
				.withEndAction(new Runnable() {

					@Override
					public void run() {
						list.remove(item);
						adapter.notifyDataSetChanged();
						view.setAlpha(1);
					}
					
				});
			}

		});
		
		return rootView;
		
		
	}
	
}
