/**
 * 
 */
package render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @author josh
 *
 */
public class AxisView extends View {

	private int width;
	
	private Paint paint;
	
	public AxisView(Context context) {
		super(context);
	}
	
    public AxisView(Context context, int leftMargin, int topMargin, int width, int height) {
    	super(context);
    	this.width = width;
    	paint = new Paint();
    	LayoutParams params = new LayoutParams(width, height);
    	params.setMargins(leftMargin, topMargin, 0, 0);
    	setLayoutParams(params);
	}

	public void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLACK);
        canvas.drawLine(0, 0, width, 0, paint);
    }

}
