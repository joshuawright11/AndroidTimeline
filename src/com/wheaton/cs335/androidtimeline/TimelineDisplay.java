package com.wheaton.cs335.androidtimeline;
import model.Timeline;
import android.content.Context;
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
        
        mTextView.setText(timeline.getName());
        mTextView2.setText("Hello");
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.leftMargin = 100;
        params.topMargin = 100;
        
        addView(mTextView);
        addView(mTextView2, params);
        
		
	}

}
