package com.wheaton.cs335.androidtimeline;
import render.AtomicTextView;
import render.DurationTextView;
import model.Atomic;
import model.Category;
import model.Duration;
import model.Timeline;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 */

/**
 * @author josh
 *
 */
public class TimelineDisplay extends RelativeLayout{

	private Timeline timeline;
	
	public TimelineDisplay(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public TimelineDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public TimelineDisplay(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void initialize(Timeline timeline){
		this.timeline = timeline;
		
		test();
		
	}

	private void test() {
        TextView mTextView = new TextView(getContext());
        TextView mTextView2 = new TextView(getContext());
        DurationTextView testing = new DurationTextView(getContext(),new Duration("HEY", new Category("Hey"), null,null, 0, "YEARS"),10,10,300);
        
        mTextView.setText(timeline.getName());
        mTextView2.setText("Hello");
        mTextView2.setBackgroundColor(Color.GREEN);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = 100;
        params.topMargin = 100;
        
        mTextView2.setLayoutParams(params);
        
        addView(mTextView);
        addView(mTextView2);
        addView(testing);
		
	}

}
