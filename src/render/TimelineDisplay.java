package render;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Atomic;
import model.Duration;
import model.TLEvent;
import model.Timeline;
import model.Timeline.AxisLabel;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This is the class that renders the actual timeline onto a JFXPanel using
 * javafx. The JFXPanel is then put into the GUI (JFXPanel is the link between
 * swing and javafx). Currently this class is a little confusing (I spent a
 * large chunk of time refactoring and simplifying it, but it is still large) so
 * I'll give an overview of the process here.
 * 
 * Currently the only public method is run. This allows the class to be run on
 * the javafx thread, since java will actually throw an exception if any javafx
 * stuff happens off this thread.
 * 
 * While rendering the class initializes some variables important to the
 * rendering process, and then renders the atomic events, axis, and duration
 * events in that order. This is so the atomic events can push the axis and
 * duration events down the screen. Detailed descriptions of this process are
 * located in the method documentations. Note that if there are no events in the
 * database a blank screen gets rendered until the user adds some events.
 * 
 * NOTE: rendering long timelines with small units (i.e. 1500-2014 with days)
 * will cause an exception. It will also save to the database which means that
 * you will have to change the axisLabel of the timeline before it renders
 * properly, (EditTimeline -> AxisLabel). It won't crash the program, it just
 * won't render.
 * 
 * @author Josh Wright Created: Feb 10, 2014 Package: graphics
 * 
 *         Various online examples of the Calendar class as well as making
 *         javafx graphics were used in the making this class.
 */
public class TimelineDisplay extends RelativeLayout{

	/**
	 * The timeline associated with this TimelineRender object
	 */
	private Timeline timeline;
	
	/**
	 * ArrayLists of all the events in the timeline. Separated into durations
	 * and atomics for rendering purposes
	 */
	private ArrayList<Duration> durations;
	private ArrayList<Atomic> atomics;
	
	/**
	 * An ArrayList of all the TLEventLabels, used for selecting events
	 * TODO Might be unnecessary for Android
	 */
	private ArrayList<TLEventTextView> eventLabels;
	
	/**
	 * The AxisLabel that this TimelineRenderer will use when rendering the
	 * timeline. Essentially the unit by which the axis will be rendered.
	 */
	private AxisLabel axisLabel;
	
	/**
	 * Use in rendering with an AxisLabel of months
	 */
	private final String[] months = { "Jan", "Feb", "March", "April", "May",
			"June", "July", "Aug", "Sept", "Oct", "Nov", "Dec" };
	
	/**
	 * The number of pixels each unit (definied by axisLAbel) on the axis takes
	 * up. Currently this is constant, but could be easily changed to account
	 * for different timeline sizes.
	 */
	private int unitWidth;

	/**
	 * The y location of the next element to be rendered. Everything is rendered
	 * from the top (0) down (positive y) to avoid overlaps of events.
	 */
	private int pushDown;

	/**
	 * The min and max time on the timeline, initialized in initRange() and used
	 * for determining the range at which to render the axis
	 */
	private long minTime;
	private long maxTime;
	
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
	
	/**
	 * Essentially a substitute for the constructor.
	 * TODO clean this up if time.
	 * 
	 * @param timeline
	 */
	public void initialize(Timeline timeline){
		this.timeline = timeline;
		if (timeline.getAxisLabel() == AxisLabel.DAYS
				|| timeline.getAxisLabel() == AxisLabel.MONTHS
				|| timeline.getAxisLabel() == AxisLabel.YEARS)
			this.axisLabel = timeline.getAxisLabel();
		else
			this.axisLabel = AxisLabel.YEARS;
		atomics = new ArrayList<Atomic>();
		durations = new ArrayList<Duration>();
		eventLabels = new ArrayList<TLEventTextView>();
		if (!initRange()) {
			return;
		}
		init();
		renderTimeline();
	}

	/**
	 * Sets the unitWidth to a constant (this can be changed to depend on how
	 * many units are in the timeline) then populates the Atomic and Duration
	 * ArrayLists. While populating the lists, it also updates the minTime and
	 * maxTime. Currently it uses milliseconds, but could be modified to use the
	 * Date class's compareTo method (which I didn't know existed until after
	 * writing this).
	 */
	private void init() {
		//TODO TimelineDisplay style?
		unitWidth = 150;
		pushDown = 60; // to allow 60 pixels for title
		for (TLEvent event : timeline.getEvents()) {
			Log.d(VIEW_LOG_TAG, "MaxTime is " + new Date(maxTime).toString());
			if (event instanceof Duration) {
				durations.add((Duration) event);
				long start = ((Duration) event).getStartDate().getTime();
				long end = ((Duration) event).getEndDate().getTime();
				if (start < minTime) {
					minTime = start;
				}
				if (end > maxTime) {
					maxTime = end;
					Log.d(VIEW_LOG_TAG, "MaxTime is: " + new Date(maxTime).toString());
				}
			} else if (event instanceof Atomic) {
				atomics.add((Atomic) event);
				long date = ((Atomic) event).getStartDate().getTime();
				if (date < minTime) {
					minTime = date;
				}
				if (date > maxTime) {
					maxTime = date;
				}
			}
		}
	}
	
	/**
	 * Initializes the minTime and maxTime to the first event. This is kind of a
	 * hack but seems to be necessary. It would not be necesssary if we used the
	 * compareTo method for Dates, however.
	 * 
	 * @return if there are any events
	 */

	private boolean initRange() {
		if (timeline.getEvents() == null) { // Initializes the variables, super
											// kludgy but we can make it better
											// later if there is time
			return false;
		}
		if (timeline.getEvents()[0] instanceof Duration) {
			minTime = ((Duration) timeline.getEvents()[0]).getStartDate()
					.getTime();
			maxTime = ((Duration) timeline.getEvents()[0]).getEndDate()
					.getTime();
			Log.d(VIEW_LOG_TAG, "MaxTime is now: " + new Date(maxTime).toString());
		} else {
			minTime = ((Atomic) timeline.getEvents()[0]).getStartDate()
					.getTime();
			maxTime = ((Atomic) timeline.getEvents()[0]).getStartDate()
					.getTime();
			Log.d(VIEW_LOG_TAG, "MaxTime is now: " + new Date(maxTime).toString());
		}
		return true;
	}
	
	/**
	 * Renders the timeline in order of height on the screen; atomic events,
	 * axis, duration events.
	 * 
	 * Also clears the old rendering and resets the group to remove the previous
	 * render.
	 */

	private void renderTimeline() {
		removeAllViews();
		addView(createTitle());

		renderTime();
		renderAtomics();
		renderDurations();
		renderLines(); //Renders the axis detail
		renderTime2(); //Renders the axis detail
		getLayoutParams().height = pushDown; //TODO Might not work
		eventsToFront();
		pushDown = 60; //For re-rendering
		requestLayout();
	}
	
	/**
	 * Sets the icon graphics of all event labels and displays them.
	 */
	private void eventsToFront() {
		for (TLEventTextView label : eventLabels) {
			if (label.getIcon() != null)
				//label.setGraphic(new ImageView(label.getIcon())); TODO ICONS
			label.bringToFront();
		}
		
	}
	
	/**
	 * Renders each 'Unit' on the axis as label with width unitWidth (uses
	 * unitLabel method). Adds the label to the group, and when finished puts
	 * the group in a scene and displays the scene in the fxPanel.
	 */
	private void renderTime() {
		int diffUnit = getUnitLength();
		int xPos2 = 0;
		for (int i = 0; i < diffUnit; i++) {
			AxisView axisSection = new AxisView(getContext(), xPos2, pushDown, unitWidth, 400); 
			Bitmap result = Bitmap.createBitmap(unitWidth, 5, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			axisSection.draw(canvas);
			addView(axisSection);
			
			TextView label = unitLabel(i, xPos2); //MUST BE AFTER
			addView(label);
			
			xPos2 += unitWidth;
		}
		pushDown += 10;
		getLayoutParams().width = (xPos2 + 5);
		requestLayout();
	}
	
	private void renderTime2() {
		int diffUnit = getUnitLength();
		int xPos2 = 0;
		for (int i = 0; i < diffUnit; i++) {
			TextView label = unitLabel(i, xPos2); //MUST BE AFTER
			addView(label);
			xPos2 += unitWidth;
		}
		pushDown += 36; //guess and check
		xPos2 = 0;
		for (int i = 0; i < diffUnit; i++) {
			AxisView axisSection = new AxisView(getContext(), xPos2, pushDown, unitWidth, 5); 
			Bitmap result = Bitmap.createBitmap(unitWidth, 5, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			axisSection.draw(canvas);
			addView(axisSection);
			
			xPos2 += unitWidth;
		}
		pushDown += 5;
		getLayoutParams().width = (xPos2 + 5);
		requestLayout();
	}
	
	/**
	 * Renders lines between the two axis.
	 * 
	 */
	private void renderLines() {
		int diffUnit = getUnitLength();
		int xPos2 = 0;
		for (int i = 0; i < diffUnit; i++) {

			AxisSeparator axisSection = new AxisSeparator(getContext(), xPos2, 63, 2, pushDown); 
			Bitmap result = Bitmap.createBitmap(5, pushDown+4, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			axisSection.draw(canvas);
			addView(axisSection);

			xPos2 += unitWidth;
		}
		getLayoutParams().width = (xPos2 + 5);
		requestLayout();
	}
	
	/**
	 * Helper method for creating units on the axis. Uses a Calendar object to
	 * add i units to the object, and then create a label with the correct
	 * position and text based on the unit and the current AxisLabel. Some
	 * stylistic things at the end.
	 * 
	 * @param i
	 *            the number of units after the first unit on the axis (the 'x'
	 *            coordinate in AxisLabel units)
	 * @param xPos2
	 *            the actual pixel starting x coordinate of the label on the
	 *            scene
	 * @return the created Label to add to the Group
	 */

	private TextView unitLabel(int i, int xPos2) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getFirstDate());
		cal.add(getUnit(), i); // adds i units to the date
		final TextView label;

		switch (axisLabel) {
		case DAYS:
			label = new TextView(getContext());
			label.setText(new Date(cal.getTime().getTime()).toString());
			break;
		case MONTHS:
			label = new TextView(getContext());
			label.setText(months[cal.get(Calendar.MONTH)] + " "
					+ cal.get(Calendar.YEAR));
			break;
		case YEARS:
			label = new TextView(getContext());
			label.setText(cal.get(Calendar.YEAR) + "");
			break;
		default:
			label = new TextView(getContext());
			label.setText("ERROR LOADING AXISLABEL");
			break;
		}
		
		LayoutParams params = new LayoutParams(unitWidth, 40);
		params.leftMargin = xPos2;
		params.topMargin = pushDown;
		label.setLayoutParams(params);
		label.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		//TODO LABEL DESIGN
		return label;
	}
	
	/**
	 * Uses a Calendar object to calculate how long the timeline will be (rounds
	 * down to the nearest unit and then finds the number of units from there to
	 * the last event, rounded up).
	 * 
	 * @return the total units long the axis will be.
	 */
	private int getUnitLength() {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(getFirstDate());
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(new Date(maxTime));
		
		Log.d(VIEW_LOG_TAG, getFirstDate().toString() + "is start end is " + new Date(maxTime).toString());

		int diffYear = endCalendar.get(Calendar.YEAR)
				- startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
				- startCalendar.get(Calendar.MONTH);
		int diffDay = diffYear * 365 + endCalendar.get(Calendar.DAY_OF_YEAR)
				- startCalendar.get(Calendar.DAY_OF_YEAR);

		switch (axisLabel) { // +1 to round up
		case DAYS:
			return diffDay + 1;
		case MONTHS:
			return diffMonth + 1;
		case YEARS:
			return diffYear + 1;
		default:
			return 0;
		}
	}
	
	/**
	 * @return the Date of the first date in the timeline rounded down to the
	 *         nearest unit. Used for drawing the axis.
	 * 
	 *         i.e. if the first date was November 3, 2012, this would return
	 *         January 1, 2012.
	 */
	private Date getFirstDate() {
		Date date = new Date(minTime);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		Date toReturn = null;

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		switch (axisLabel) {
		case DAYS:
			cal.set(year, month, day);
			toReturn = new Date(cal.getTime().getTime());
			break;
		case MONTHS:
			cal.set(year, month, 1);
			toReturn = new Date(cal.getTime().getTime());
			break;
		case YEARS:
			cal.set(year, 0, 1);
			toReturn = new Date(cal.getTime().getTime());
			break;
		default:
			break;
		}
		return toReturn;
	}

	/**
	 * Creates labels out of each Atomic event to add to the Group. Calculates x
	 * position based on the date and y position based on the pushDown global
	 * value.
	 * 
	 * Uses custom Label class
	 */
	private void renderAtomics() {
		pushDown += 30;
		for (Atomic e : atomics) {
			int xPosition = getXPos(e.getStartDate());
			AtomicTextView label = new AtomicTextView(getContext(), e, xPosition, pushDown);
			eventLabels.add(label);
			addView(label);
			pushDown += 25;
		}
		// add a little space between the atomic events and the axis
	}
	
	/**
	 * Creates labels out of each Duration event to add to the Group. Calculates
	 * x position based on start and end date and y position based on the
	 * pushDown global value.
	 * 
	 * Uses custom Label class
	 */
	private void renderDurations() {
		int counter = 0;
		pushDown -= 25; // add a little space between the axis and the duration
						// events
		for (Duration e : durations) {
			int xStart = getXPos(e.getStartDate());
			int xEnd = getXPos(e.getEndDate());
			int labelWidth = xEnd - xStart;

			DurationTextView label = new DurationTextView(getContext(), e, xStart, (pushDown + 45 + counter), labelWidth);
			eventLabels.add(label);
			addView(label);
			pushDown += 25;
		}
		pushDown += 65;
	}
	
	/**
	 * Returns the pixel x position that a date should be, based on its value
	 * and the axis
	 * 
	 * @param date
	 *            the date to get the x position for
	 * @return the pixel x value that the date should be
	 */
	private int getXPos(Date date) {
		double units = getUnitsSinceStart(date);
		int xPosition = (int) (units * unitWidth);
		return xPosition;
	}
	
	/**
	 * Returns the number of units (based on axisLabel) since the first date on
	 * the timeline axis (see getFirstDate) for a Date.
	 * 
	 * i.e. if first date was January 1, 2011, date was January 1, 2012 this and
	 * axisLabel was days, this would return 366.
	 * 
	 * @param date
	 *            the date to get the units for
	 * @return the units since the start date, of the date
	 */
	private double getUnitsSinceStart(Date date) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(getFirstDate());
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(date);

		double diffYear = endCalendar.get(Calendar.YEAR)
				- startCalendar.get(Calendar.YEAR);
		double diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
				- startCalendar.get(Calendar.MONTH);
		double diffDay = diffYear * 365 + endCalendar.get(Calendar.DAY_OF_YEAR)
				- startCalendar.get(Calendar.DAY_OF_YEAR);

		double years = diffYear
				+ (endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar
						.get(Calendar.DAY_OF_YEAR)) / 365.0;
		double months = diffMonth
				+ (endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar
						.get(Calendar.DAY_OF_MONTH)) / 30.5;
		double days = diffDay + 0.5; // so that dates lineup nicely

		switch (axisLabel) {
		case DAYS:
			return days;
		case MONTHS:
			return months;
		case YEARS:
			return years;
		default:
			return 0;
		}
	}

	/**
	 * Returns the calendar unit based on axisLabel (used in rendering the
	 * different pieces based on length and date)
	 * 
	 * @return the Calendar unit
	 */
	private int getUnit() {
		switch (axisLabel) {
		case DAYS:
			return Calendar.DATE;
		case MONTHS:
			return Calendar.MONTH;
		case YEARS:
			return Calendar.YEAR;
		default:
			return 0;
		}
	}
	
	/**
	 * Makes a very simple title for the timeline
	 * 
	 * @return A Text object that will be the title
	 */
	private TextView createTitle() {
		TextView t = new TextView(getContext());
		t.setText(timeline.getName());
		t.setX(20);
		t.setY(20);
		//TODO MAKE THIS LOOK PRETTY... also might not work. 
		return t;
	}
	
}
