package render;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import model.Category;
import model.TLEvent;

/**
 * An abstract class to create labels for Atomic and Duration events to render.
 * Currently the subclasses have a decent amount of repetition so some of that could be
 * moved up here.
 * 
 * @author Josh Wright
 * Created: February 15, 2014
 * Last Edited: March 7, 2014
 * 
 * Some ContextMenu code ripped from Oracle's documentation on ContextMenus
 */
public abstract class TLEventTextView extends TextView {
	
	/**
	 * The event associated with this label
	 */
	private TLEvent event;
	
	/**
	 * The x and y position of this event
	 */
	private int xPos;
	private int yPos;
        
    private Drawable icon;
	
//	private ContextMenu contextMenu;
	
	/**
	 * Set the text of the label to text
	 * 
	 * @param text the label text
	 */
	TLEventTextView(Context context, int xPos, int yPos, TLEvent event){
		super(context);
		this.event = event;
		this.xPos = xPos;
		this.yPos = yPos;
                if(event.getIcon()!=null)
                    this.icon = event.getIcon().getImage();
		//contextMenu = new ContextMenu();
		init();
	}
        
        public Drawable getIcon(){
            return icon;
        }
	
	/**
	 * Initializes generic parts of TLEventLabel
	 */
	private void init(){
		initDesign();
		//initContextMenu();
		initHandlers();
	}

//	/**
//	 * Initializes the ContextMenu which will display when the item is clicked
//	 */
//	private void initContextMenu() {
//		contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
//		    public void handle(WindowEvent e) {
//		        //System.out.println("showing");
//		    }
//		});
//		
//		Text t = new Text();
//		t.setText(event.getName());
//		t.setFont(Font.font("Verdana",20));
//		t.setFill(Color.BLACK);
//		CustomMenuItem name = new CustomMenuItem(t);
//		
//		TextArea test = new TextArea();
//		test.setText(event.getDescription());
//		test.setPrefWidth(200);
//		test.setEditable(false);
//		test.setWrapText(true);
//		
//		
//		if(event.getCategory() != null){
//			MenuItem category = new MenuItem("Category: "+event.getCategory().getName());
//			CustomMenuItem text = new CustomMenuItem(test);
//			contextMenu.getItems().addAll(name, category, text);
//			setContextMenu(contextMenu);
//		}
//		
//	}

	/**
	 * Sets up the "design" of the label. Border, position, etc.
	 */
	private void initDesign(){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = xPos;
		params.topMargin = yPos;
		Log.d(VIEW_LOG_TAG, event.getName() + " added with a left margin of " + xPos + " and a top margin of " + yPos);
		setLayoutParams(params);
		setText(event.getName());
		setBackgroundColor(event.getCategory().getColor());
		setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		uniqueDesign();
	}

	/**
	 * Initialize generic handlers for the TLEventLabel
	 */
	private void initHandlers(){
		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "HEY HEY", Toast.LENGTH_SHORT).show();
				// TODO SHOW MENU
			}
			
		});
		uniqueHandlers();
	}
	
	/**
	 * Abstract method where unique design code can go. 
	 */
	public abstract void uniqueDesign();

	/**
	 * Initialize handlers unique to Duration or Atomic
	 */
	public abstract void uniqueHandlers();
}
