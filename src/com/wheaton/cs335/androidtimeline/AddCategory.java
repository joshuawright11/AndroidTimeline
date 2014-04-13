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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.os.Build;

public class AddCategory extends Activity implements OnSeekBarChangeListener{
	
	private SeekBar rsb, gsb, bsb;
	private int red, green, blue;
	private TextView categoryTitle;
	private boolean initialized;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_category);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		//setupSeekBars(dialogView);
	}

	private void setupSeekBars() {
		rsb = (SeekBar) findViewById(R.id.redSelecter);
		gsb = (SeekBar) findViewById(R.id.greenSelecter);
		bsb = (SeekBar) findViewById(R.id.blueSelecter);
		rsb.setOnSeekBarChangeListener(this);
		gsb.setOnSeekBarChangeListener(this);
		bsb.setOnSeekBarChangeListener(this);
		categoryTitle = (TextView) findViewById(R.id.categoryName);
	}
	
	private void updateColor() {
		red = rsb.getProgress();
		green = gsb.getProgress();
		blue = bsb.getProgress();
		categoryTitle.setTextColor(0xff000000+red*0x10000+green*0x100+blue);
	}
	
	public void okClick(View view){
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			View rootView = inflater.inflate(R.layout.fragment_add_category,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(!initialized){
			setupSeekBars();
		}
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
