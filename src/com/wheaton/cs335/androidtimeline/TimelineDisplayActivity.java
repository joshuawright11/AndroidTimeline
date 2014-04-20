/**
 * 
 */
package com.wheaton.cs335.androidtimeline;

import model.Timeline;
import android.app.Activity;
import android.os.Bundle;

/**
 * @author josh
 *
 */
public class TimelineDisplayActivity extends Activity {

	private Timeline timeline;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_timeline);
        
        //ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
        TimelineDisplay viewGroup = (TimelineDisplay) findViewById(R.id.timelineDisplay);
        timeline = (Timeline) getIntent().getSerializableExtra("TIMELINE");
        viewGroup.initialize(timeline);
        
    }

	
}
