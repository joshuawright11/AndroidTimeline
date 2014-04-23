package com.wheaton.cs335.androidtimeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.parser.ParseException;

import storage.DBHelperAPI;
import storage.phpPushHelper;
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
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;
import model.Category;
import model.Timeline;

public class AddCategory extends Activity{
	
	private SeekBar rsb, gsb, bsb;
	private int red, green, blue;
	PlaceholderFragment phf;
	private TextView categoryTitle;
	private boolean initialized;
	
	static private Spinner CatSelector;
	
	static private Activity thisActivity;
	
	static ArrayList<Timeline> timelines;
	
	static DBHelperAPI database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_category);
		if (savedInstanceState == null) {
			phf = new PlaceholderFragment();
			phf.sl = new SeekListener();
			rsb = phf.rsb;
			gsb = phf.gsb;
			bsb = phf.bsb;
			getFragmentManager().beginTransaction()
					.add(R.id.container, phf).commit();
		}
		
		thisActivity = this;
		
		timelines = (ArrayList<Timeline>) getIntent().getSerializableExtra("timelines");
	}
	
	private class SeekListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			updateColor();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {}
		
	}
	
	private void updateColor() {
		rsb = (SeekBar) findViewById(R.id.redSelecter);
		gsb = (SeekBar) findViewById(R.id.greenSelecter);
		bsb = (SeekBar) findViewById(R.id.blueSelecter);
		red = rsb.getProgress();
		green = gsb.getProgress();
		blue = bsb.getProgress();
		rsb.setBackgroundColor(Color.rgb(red,green,blue));
		gsb.setBackgroundColor(Color.rgb(red,green,blue));
		bsb.setBackgroundColor(Color.rgb(red,green,blue));
	}
	
	public void okClick(View view){
		rsb = (SeekBar) findViewById(R.id.redSelecter);
		gsb = (SeekBar) findViewById(R.id.greenSelecter);
		bsb = (SeekBar) findViewById(R.id.blueSelecter);
		red = rsb.getProgress();
		green = gsb.getProgress();
		blue = bsb.getProgress();
		
		Category cat = new Category(((TextView) findViewById(R.id.categoryName)).getText().toString(),Color.rgb(red,green,blue));
		Timeline selected = timelines.get(CatSelector.getSelectedItemPosition());
		selected.addCategory(cat);
		/*try {
			phpPushHelper.send(timelines);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}*/

		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_category, menu);
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private SeekBar rsb, gsb, bsb;
		private SeekListener sl;
		public PlaceholderFragment(){}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_category,
					container, false);
			
			//uncomment when concrete DBHelper is available
			ArrayList<String> timeNames = new ArrayList<String>();
			for(Timeline t : timelines)
				timeNames.add(t.getName());
			ArrayAdapter<String> adp1;
			CatSelector = (Spinner) rootView.findViewById(R.id.addCategoryTimelineSelector);
			adp1 = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, timeNames);
			CatSelector.setAdapter(adp1);
			
			rsb = (SeekBar) rootView.findViewById(R.id.redSelecter);
			gsb = (SeekBar) rootView.findViewById(R.id.greenSelecter);
			bsb = (SeekBar) rootView.findViewById(R.id.blueSelecter);
			
			rsb.setMax(255); rsb.setProgress(255); rsb.setBackgroundColor(Color.WHITE);
			gsb.setMax(255); gsb.setProgress(255); gsb.setBackgroundColor(Color.WHITE);
			bsb.setMax(255); bsb.setProgress(255); bsb.setBackgroundColor(Color.WHITE);
			
			rsb.setOnSeekBarChangeListener(sl);
			gsb.setOnSeekBarChangeListener(sl);
			bsb.setOnSeekBarChangeListener(sl);
			
			return rootView;
		}
	}
}
