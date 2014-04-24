/**
 * 
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


/**
 * Timeline.java
 * 
 * Timeline object to keep track of the different timelines in the project.
 * Contains a name, ArrayList of TLEvents, AxisLabel (for rendering), and
 * boolean, dirty, which is updated whenever the Timeline is changed. This can
 * be used for deciding when to sync to the database, but is currently not in
 * use (we sync whenever certain buttons in the GUI are pressed).
 * 
 * @author Josh Wright and Andrew Thompson Wheaton College, CS 335, Spring 2014
 *         Project Phase 1 Feb 15, 2014
 */
public class Timeline implements TimelineAPI,Serializable {

	/**
	 * ArrayList to keep track of the events in the timeline
	 */
	private ArrayList<TLEvent> events;

	/**
	 * Name of the timeline
	 */
	private String name;

	/**
	 * A list of all the categories in this application.
	 */
	private ArrayList<Category> categories;

	/**
	 * The selected Category of this timein
	 */
	private Category selectedCategory;

	/**
	 * The unique id of this timeline (for database saving)
	 */
	private int id;

	/**
	 * enum for keeping track of the potential units to render the timeline in
	 * currently only DAYS, MONTHS, and YEARS work, but implementing the others
	 * would be very simple
	 */
	public static enum AxisLabel {
		DAYS, WEEKS, MONTHS, YEARS, DECADES, CENTURIES, MILLENNIA;
	}

	/**
	 * Array of the AxisLabels, for getting the value based on an index
	 */
	private static final AxisLabel[] AXIS_LABELS = { AxisLabel.DAYS,
			AxisLabel.WEEKS, AxisLabel.MONTHS, AxisLabel.YEARS,
			AxisLabel.DECADES, AxisLabel.CENTURIES, AxisLabel.MILLENNIA };

	/**
	 * The units to render the timeline in
	 */
	private AxisLabel axisLabel;
	/**
	 * The Color of the timeline
	 */
	private String colorTL;
	/**
	 * The Color of the Background
	 */
	private String colorBG;
	/**
	 * whether the timeline has been changed since its last database sync
	 */
	private boolean dirty;

	/**
	 * Constructor for name and axisLabel
	 * 
	 * @param name
	 *            Timeline name
	 */
	public Timeline(String name) {
		this.name = name;
		events = new ArrayList<TLEvent>();
		axisLabel = AxisLabel.YEARS;
		setDirty(true);
		categories = new ArrayList<Category>();
		categories.add(new Category("DEFAULT"));
	}

	/**
	 * Constructor for name and axisLabel
	 * 
	 * @param name
	 *            Timeline name
	 * @param axisLabel
	 *            Unit to render timeline in
	 */
	public Timeline(String name, AxisLabel axisLabel, String colorTL,
			String colorBG) {
		this.name = name;
		this.colorBG = colorBG;
		this.colorTL = colorTL;
		events = new ArrayList<TLEvent>();
		this.axisLabel = axisLabel;
		this.events = new ArrayList<TLEvent>();
		dirty = true;
		categories = new ArrayList<Category>();
		categories.add(new Category("DEFAULT"));
	}

	/**
	 * Constructor for name, events, and axisLabel
	 * 
	 * @param name
	 *            Timeline name
	 * @param events
	 *            TLEvents in timeline
	 * @param axisLabel
	 *            Unit to render timeline in
	 */
	public Timeline(String name, TLEvent[] events, String colorTL,
			String colorBG, AxisLabel axisLabel) {
		categories = new ArrayList<Category>();
		categories.add(new Category("DEFAULT"));
		this.name = name;
		if (events != null)
			this.events = new ArrayList<TLEvent>(Arrays.asList(events));
		else
			this.events = new ArrayList<TLEvent>();
		this.axisLabel = axisLabel;
		this.colorBG = colorBG;
		this.colorTL = colorTL;
		dirty = true;

	}

	/**
	 * Return the unique ID of this event for the database.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Set the unique ID of this event for the database.
	 * 
	 * @param id
	 *            The id to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Returns true if this timeline contains TLEvent event.
	 * 
	 * @param event
	 *            The event to see if this Timeline contains
	 * @return True if this timeline contains the event, false if not
	 */
	@Override
	public boolean contains(TLEvent event) {
		for (TLEvent e : events)
			if (e.equals(event))
				return true;
		return false;
	}

	/**
	 * Finds and returns an event
	 * 
	 * @param title
	 *            The name of the event to return.
	 * @return The TLEvent found.
	 * @throws Exception
	 *             If not found, throws this exception.
	 */
	public TLEvent findEvent(String title) throws Exception {
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getName().equals(title))
				return events.get(i);
		}
		throw new Exception("Not found.");
	}

	/**
	 * Adds an event to the ArrayList of TLEvents.
	 * 
	 * @param event
	 *            The event to add.
	 */
	@Override
	public void addEvent(TLEvent event) {
		setDirty(true);
		events.add(event);
	}

	/**
	 * Removes an event from the ArrayList of TLEvents.
	 * 
	 * @param event
	 *            The event to delete.
	 */
	@Override
	public boolean removeEvent(TLEvent event) {
		if (events.contains(event)) {
			events.remove(event);
			setDirty(true);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes @param oldEvent and replaces it with @param newEvent from the
	 * ArrayList.
	 */
	@Override
	public boolean changeEvent(TLEvent oldEvent, TLEvent newEvent) {
		if (events.contains(oldEvent)) {
			events.remove(oldEvent);
			events.add(newEvent);
			setDirty(true);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns an array representation of all TLEvents.
	 */
	@Override
	public TLEvent[] getEvents() {
		if (events.isEmpty())
			return null;
		return (TLEvent[]) events.toArray(new TLEvent[events.size()]);
	}

	/**
	 * Returns true if the Timeline is dirty, false otherwise.
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the boolean dirty.
	 */
	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * Returns the name of this Timeline.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns this Timeline's axis label index.
	 */
	@Override
	public int getAxisLabelIndex() {
		for (int i = 0; i < AXIS_LABELS.length; i++)
			if (AXIS_LABELS[i] == axisLabel)
				return i;
		return -1;
	}

	/**
	 * 
	 * @return The Color BG of this Timeline.
	 */
	public String getColorBG() {
		return colorBG;
	}

	/**
	 * 
	 * @return The Color of this Timeline.
	 */
	public String getColorTL() {
		return colorTL;
	}

	/**
	 * 
	 * @param colorBG
	 *            Sets the color of this Timeline's background.
	 */
	public void setColorBG(String colorBG) {
		this.colorBG = colorBG;
	}

	/**
	 * 
	 * @param colorTL
	 *            Sets the color of this Timeline.
	 */
	public void setColorTL(String colorTL) {
		this.colorTL = colorTL;
	}

	/**
	 * @return The AxisLabel of this Timeline.
	 */
	@Override
	public AxisLabel getAxisLabel() {
		return axisLabel;
	}

	/**
	 * 
	 * @param axisLabel
	 *            Sets the AxisLabel of this Timeline.
	 */
	public void setAxisLabel(AxisLabel axisLabel) {
		this.axisLabel = axisLabel;
	}

	/**
	 * 
	 * @return the built-in iterator of the ArrayList events.
	 */
	public Iterator<TLEvent> getEventIterator() {
		return events.iterator();
	}

	/**
	 * Adds a Category to the current collection of Categories.
	 * 
	 * @param c
	 *            The Category to add.
	 * @return True if successful, False otherwise.
	 */
	public boolean addCategory(Category c) {
		if (containsTitle(c))
			return false;
		// Replace the default category if it wasn't edited.
		else if (categories.size() == 1
				&& getDefaultCategory().getName().equals("DEFAULT")) {
			categories.add(c);
			deleteCategory(getDefaultCategory());
		} else
			categories.add(c);
		return true;
	}

	/**
	 * Searches known category titles to find a match.
	 * 
	 * @param cat
	 *            The category for which to search.
	 * @return True if found, False otherwise.
	 */
	private boolean containsTitle(Category cat) {
		for (Category c : categories) {
			if (c.getName().equals(cat.getName()))
				return true;
		}
		return false;
	}

	/**
	 * Deletes a Category from the current set of categories.
	 * 
	 * @param cat
	 *            The category to delete.
	 * @return True if found and removed, False otherwise.
	 */
	public boolean deleteCategory(Category cat) {
		// The user is not allowed to delete the only category!
		if (categories.size() <= 1)
			return false;
		for (TLEvent e : events)
			if (e.getCategory().getName().equals(cat.getName()))
				e.setCategory(getDefaultCategory());
		selectCategory(getDefaultCategory().getName());
		return categories.remove(cat);
	}

	/**
	 * Method to get the default category.
	 * 
	 * @return The default Category.
	 */
	public Category getDefaultCategory() {
		return categories.get(0);
	}

	/**
	 * 
	 * @return The default iterator that comes with the categories ArrayList.
	 */
	public Iterator<Category> getCategoryIterator() {
		return categories.iterator();
	}

	/**
	 * 
	 * @return a String ArrayList of all category titles.
	 */
	public ArrayList<String> getCategoryTitles() {
		ArrayList<String> toReturn = new ArrayList<String>();
		for (Category c : categories) {
			toReturn.add(c.getName());
		}
		return toReturn;
	}

	/**
	 * 
	 * @return The currently selected category.
	 */
	public Category getSelectedCategory() {
		if (selectedCategory == null)
			selectedCategory = getDefaultCategory();
		return selectedCategory;
	}

	/**
	 * 
	 * @param title
	 *            The Category to find.
	 * @return the Category that matches up with param title in the list of
	 *         categories.
	 */
	public Category getCategory(String title) {
		for (Category c : categories)
			if (c.getName().equals(title))
				return c;
		return getDefaultCategory();
	}

	/**
	 * Edits the category to change the parameter fields
	 * 
	 * @param title
	 *            The title of the category to be edited.
	 * @param name
	 *            The new name of this category.
	 * @param color
	 *            The new color of this category.
	 */
	public void editCategory(String title, String name, String color) {
		Category c = getCategory(title);
		c.setColor(color);
		c.setName(name);
	}

	/**
	 * 
	 * @param title
	 *            Changes the selected category to the category that matches
	 *            this title.
	 */
	public void selectCategory(String title) {
		selectedCategory = getCategory(title);
	}

}
