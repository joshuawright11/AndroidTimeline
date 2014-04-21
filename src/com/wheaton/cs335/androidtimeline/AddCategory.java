package com.wheaton.cs335.androidtimeline;

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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.os.Build;
import model.Category;

public class AddCategory extends Activity{
	
	private SeekBar rsb, gsb, bsb;
	private int red, green, blue;
	PlaceholderFragment phf;
	private TextView categoryTitle;
	private boolean initialized;

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
	}
	
	private class SeekListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			updateColor();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
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
		//save category
		
		Intent intent = new Intent(this, MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
	    startActivity(intent);
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
