/**
 * 
 */
package com.wheaton.cs335.androidtimeline;

import model.Timeline;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author josh
 *
 */
public class TimelineDisplayActivity extends Activity {

	private Timeline timeline;
	
	private TextView mTextView = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextView = new TextView(this);

        Timeline timeline = (Timeline) getIntent().getSerializableExtra("TIMELINE");

        if (savedInstanceState == null) {
        	mTextView.setText(timeline.getName());
        } else {
            mTextView.setText(timeline.getName());
        }
        

        setContentView(mTextView);
    }

	
}
