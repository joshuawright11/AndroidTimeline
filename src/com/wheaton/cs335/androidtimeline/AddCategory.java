package com.wheaton.cs335.androidtimeline;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import model.Category;
import model.Timeline;

/**
 * AddCategory is an Activity used for adding categories to timelines. This activity provides
 * a clean workflow for naming a category, selecting a color via three sliders each of which
 * represent the red, green, and blue components of a composite color. The user can add this
 * category to any of the preexisting timelines of the user. Finally, an OK button is
 * available to finalize the addition of the category.
 */
public class AddCategory extends Activity{
	
	/**
	 * The Seekbars for determining Color.
	 */
	private SeekBar rsb, gsb, bsb;
	
	/**
	 * The color components
	 */
	private int red, green, blue;
	
	/**
	 * This is used to create the Fragment to be displayed in the Activity
	 */
	private PlaceholderFragment phf;
	
	/**
	 * This is the spinner used to select the timeline to which to add the category.
	 */
	private static Spinner timeSelector;
	
	/**
	 * This is the array of timelines as received from MainActivity
	 */
	private static ArrayList<Timeline> timelines;

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * This method creates a new Activity for AddCategory.
	 */
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
		
		timelines = (ArrayList<Timeline>) getIntent().getSerializableExtra("timelines");
	}
	
	/**
	 * This is an OnSeekBarChangeListener class used to update the color of the seek bars.
	 */
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
	
	/**
	 * This method is used to retrieve the seekbars, set the color components as their progress,
	 * and finally set the background color of all the seekbars to the composite color.
	 */
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
	
	/**
	 * This method is called when a button is clicked in this Activity. In particular, this means
	 * that when the OK button is selected, the composite color is computed from the seekbars,
	 * and then a new category is created with the name of the Category derived from the TextView.
	 * This category is then added to the selected timeline. The database is updated, and then the
	 * finish() is called in the Activity.
	 * 
	 * @param view
	 */
	public void okClick(View view){
		rsb = (SeekBar) findViewById(R.id.redSelecter);
		gsb = (SeekBar) findViewById(R.id.greenSelecter);
		bsb = (SeekBar) findViewById(R.id.blueSelecter);
		red = rsb.getProgress();
		green = gsb.getProgress();
		blue = bsb.getProgress();
		
		Category cat = new Category(((TextView) findViewById(R.id.categoryName)).getText().toString(),Color.rgb(red,green,blue));
		Timeline selected = timelines.get(timeSelector.getSelectedItemPosition());
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
	/**
	 * This method creates the menu for the Activity.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_category, menu);
		return true;
	}

	/**
	 * This is the fragment to be added to the Activity.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * These are the seekbars of the fragment, used to determine color.
		 */
		private SeekBar rsb, gsb, bsb;
		
		/**
		 * This is the listener to be attached to the seekbars.
		 */
		private SeekListener sl;
		
		/**
		 * Constructor
		 */
		public PlaceholderFragment(){}


		@Override
		/**
		 * This method adds the Fragment to the Activity.
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_add_category,
					container, false);
			
			ArrayList<String> timeNames = new ArrayList<String>();
			for(Timeline t : timelines)
				timeNames.add(t.getName());
			ArrayAdapter<String> adp1;
			timeSelector = (Spinner) rootView.findViewById(R.id.addCategoryTimelineSelector);
			adp1 = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, timeNames);
			timeSelector.setAdapter(adp1);
			
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
