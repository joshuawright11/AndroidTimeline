//package model;
//
//import storage.*;
////import javafx.scene.paint.Color;
////import javafx.scene.text.Font;
//import model.Timeline.AxisLabel;
//
//import java.sql.Date;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
///**
// * TimelineMaker.java
// * 
// * The model of the timeline editor and viewer. Contains all necessary objects
// * to model the application.
// * 
// * @author Josh Wright and Andrew Thompson Wheaton College, CS 335, Spring 2014
// *         Project Phase 1 Feb 15, 2014
// * 
// */
//public class TimelineMaker {
//	/**
//	 * A list of all the timelines in this application.
//	 */
//	private ArrayList<Timeline> timelines;
//	private ArrayList<Icon> icons;
//	HashMap<Integer, Icon> iconMap;
//	/**
//	 * The timeline selected in this application.
//	 */
//	private Timeline selectedTimeline;
//	/**
//	 * The event selected in this application.
//	 */
//	private TLEvent selectedEvent;
//	/**
//	 * The database for storing timelines of this application.
//	 */
//	private DBHelper database;
//	/**
//	 * The main GUI window for this application.
//	 */
//	private MainWindowController mainWindow;
//
//	/**
//	 * The graphics object for displaying timelines in this application.
//	 */
//	public TimelineGraphics graphics;
//	int idCounter;
//
//	private final String help_text = "\tHow to use this Timeline Maker:  \n"
//			+ "*Use the buttons on the left to create, edit, or delete timelines. Timelines may have titles and background colors, and they may be displayed in a number of different units.\n"
//			+ "*IMPORTANT: Timeline dates are in year-month-day form. You have to include the dashes.\n"
//			+ "*Each timeline has a set of events. Create events with the \"add\" button.\n"
//			+ "*To edit and delete events, select them on the rendered timeline and then proceed to delete them.\"\n"
//			+ "*Each timeline also has a set of categories. There must be at least one category, the default category, which may be edited. Each category has a name and a color associated with it.\"\n"
//			+ "*Image icons may be added to timeline events. Upload images using the right side-bar and set them in the event editing window.\n"
//			+ "*Double click events for surprises!";
//
//	private final String about_text = "\tCredits: \n\n"
//			+ "@Authors Andrew.Sutton, Josh Wright, Kayley Lane, Conner Vick, Brian Williamson\n\n"
//			+ "\tSoftware Dev 2014";
//
//	/**
//	 * Constructor. Create a new TimelineMaker application model with database,
//	 * graphics, and GUI components.
//	 */
//	public TimelineMaker() {
//		database = new DBHelper("timeline.db");
//		graphics = new TimelineGraphics(this);
//		timelines = new ArrayList<Timeline>();
//		icons = new ArrayList<Icon>();
//		icons.add(new Icon("None", null, null));
//		try {
//			for (Timeline t : database.getTimelines())
//				timelines.add(t);
//			HashMap<Category, String> categories = database.getCategories();
//			for (Timeline t : timelines) { // Very lame. Should have better
//											// implementation but don't have
//											// time.
//				for (Category c : categories.keySet()) {
//					if (t.getName().equals(categories.get(c))) {
//						t.addCategory(c);
//					}
//				}
//			}
//			for (Timeline t : timelines) { // sets categories.
//				if (t.getEvents() == null)
//					continue;
//				for (TLEvent e : t.getEvents()) {
//					Category toSet = t.getCategory(e.getCategory().getName());
//					if (toSet != null) {
//						e.setCategory(toSet);
//					}
//				}
//			}
//			for (Icon icon : database.getIcons()) {
//				icons.add(icon);
//			}
//			populateEventIcons();
//			selectedTimeline = timelines.get(0);
//			selectedEvent = null;
//		} catch (IndexOutOfBoundsException e) {
//			System.out.println("Your database is empty.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Error loading from Database.");
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param t
//	 *            The icon to be returned
//	 * @return The icon with a name matching String t
//	 */
//	public Icon getIcon(String t) {
//		for (Icon i : icons) {
//			if (i.getName().equals(t))
//				return i;
//		}
//		return icons.get(0);
//	}
//
//	/**
//	 * 
//	 * @return An ArrayList representation of all the icon names.
//	 */
//	public ArrayList<String> getImageTitles() {
//		ArrayList<String> iconTitles = new ArrayList<String>();
//		for (Icon i : icons) {
//			iconTitles.add(i.getName());
//		}
//		return iconTitles;
//	}
//
//	/**
//	 * 
//	 * @param i
//	 *            Icon to be added to the list of icons and to the database.
//	 */
//	public void addIcon(Icon i) {
//		if (i != null) {
//			icons.add(i);
//			database.saveIcon(i);
//		}
//	}
//
//	/**
//	 * Deletes an icon from the list of icons and the database.
//	 * 
//	 * @param icon
//	 *            the name of the icon to be deleted
//	 * @return success
//	 */
//	public boolean deleteIcon(String icon) {
//		// TODO GET THIS WORKING
//		// The user is not allowed to delete the only category!
//		if (icons.size() <= 1)
//			return false;
//		Icon ico = new Icon(null, null, null);
//		for (Icon i : icons)
//			if (i.getName().equals(icon)) {
//				ico = i;
//				break;
//			}
//		for (Timeline t : timelines) {
//			Iterator<TLEvent> eventIterator = t.getEventIterator();
//			TLEvent e;
//			while (eventIterator.hasNext()) {
//				e = eventIterator.next();
//				if (e.getIcon() == ico)
//					e.setIcon(null);
//			}
//		}
//		return icons.remove(ico);
//	}
//
//	/**
//	 * Retrieve a list of the names of all the timelines.
//	 * 
//	 * @return timelines
//	 */
//	public ArrayList<String> getTimelineTitles() {
//		ArrayList<String> toReturn = new ArrayList<String>();
//		for (Timeline t : timelines) {
//			toReturn.add(t.getName());
//		}
//		return toReturn;
//	}
//
//	/**
//	 * Retrieve the timeline with the parameterized name.
//	 * 
//	 * @param title
//	 *            The name of the timeline to be found
//	 * @return The timeline with the correct name; null otherwise.
//	 */
//	private Timeline getTimeline(String title) {
//		for (Timeline t : timelines)
//			if (t.getName().equals(title))
//				return t;
//		return null;
//	}
//
//	/**
//	 * 
//	 * @return the currently selected timeline.
//	 */
//	public Timeline getSelectedTimeline() {
//		return selectedTimeline;
//	}
//
//	/**
//	 * Set the selected timeline. Find the timeline of the parameterized title
//	 * and set selectedTimeline to it. Update selectedTimeline, selectedTLEvent,
//	 * and graphics.
//	 * 
//	 * @param title
//	 *            of the timeline
//	 */
//	public void selectTimeline(String title) {
//		selectedTimeline = getTimeline(title);
//		selectedEvent = null;
//		if (selectedTimeline != null)
//			updateGraphics();
//	}
//
//	/**
//	 * Selects the default timeline in the case that an event is deleted.
//	 */
//	public void selectDefaultTimeline() {
//		if (timelines.size() > 0)
//			selectedTimeline = timelines.get(0);
//		else
//			selectedTimeline = null;
//	}
//
//	/**
//	 * Add a timeline to this model. Update selectedTimeline, selectedTLEvent,
//	 * graphics, and database.
//	 * 
//	 * @param t
//	 *            the timeline to be added
//	 */
//	public void addTimeline(String title, Color colorTL, Color colorBG,
//			AxisLabel axisUnit, Font font) {
//		Timeline t = new Timeline(title, axisUnit, colorTL, colorBG);
//		selectedTimeline = t;
//		selectedEvent = null;
//		timelines.add(selectedTimeline);
//
//		database.saveTimeline(selectedTimeline);
//		//mainWindow.populateListView();
//		// gui.updateTimelines(getTimelineTitles(), selectedTimeline.getName());
//		updateGraphics();
//	}
//
//	/**
//	 * Remove a timeline from this model. Update selectedTimeline,
//	 * selectedTLEvent, graphics, and database.
//	 * 
//	 * @param t
//	 *            the timeline to be removed
//	 */
//	public void deleteTimeline() {
//		if (selectedTimeline != null) {
//			timelines.remove(selectedTimeline);
//			database.removeTimeline(selectedTimeline);
//			selectedTimeline = null;
//			selectedEvent = null;
//			graphics.clearScreen();
//			mainWindow.populateListView();
//		}
//	}
//
//	/**
//	 * Edit the selected timeline. Remove the selected timeline and replace it
//	 * with the parameterized one. Update selectedTimeline, selectedTLEvent,
//	 * graphics, and database.
//	 * 
//	 * @param t
//	 *            the new timeline
//	 */
//
//	public void editTimeline(Timeline t, String title, Color colorTL,
//			Color colorBG, AxisLabel axisUnit, Font font) {
//		timelines.remove(selectedTimeline);
//		Timeline newTimeline = new Timeline(title, t.getEvents(), colorTL,
//				colorBG, axisUnit);
//		newTimeline.setID(t.getID());
//		timelines.add(newTimeline);
//		database.editTimelineInfo(newTimeline);
//		selectedTimeline = newTimeline;
//		//mainWindow.populateListView();
//		updateGraphics();
//	}
//
//	/**
//	 * Populates the list view of the main window.
//	 */
//	public void populateView() {
//		mainWindow.populateListView();
//	}
//
//	/**
//	 * Retrieve the currently selected event.
//	 * 
//	 * @return selectedTLEvent
//	 */
//	public TLEvent getSelectedEvent() {
//		return selectedEvent;
//	}
//
//	/**
//	 * Set the selected event.
//	 * 
//	 * @param e
//	 *            The event to be selected
//	 */
//	public void selectEvent(TLEvent e) {
//		if (e != null)
//			selectedEvent = e;
//	}
//
//	/**
//	 * Add an event to the selected timeline. Update selectedTimeline,
//	 * selectedTLEvent, graphics, and database.
//	 * 
//	 * @param e
//	 *            the new event
//	 */
//
//	public void addEvent(String title, Date startDate, Date endDate,
//			Object category, String description, Icon icon) {
//		TLEvent event;
//		if (endDate != null) {
//			event = new Duration(title, selectedTimeline.getDefaultCategory(),
//					startDate, endDate, icon.getId(), description);
//		} else {
//			event = new Atomic(title, selectedTimeline.getDefaultCategory(),
//					startDate, icon.getId(), description);
//		}
//		if (!icon.getName().equals("None") || event.getIcon() == null) {
//			event.setIcon(icon);
//		}
//		if (selectedTimeline != null) {
//			selectedTimeline.addEvent(event);
//			selectedEvent = event;
//			updateGraphics();
//			database.saveEvent(event, selectedTimeline.getName());
//		}
//	}
//
//	/**
//	 * Delete the selected event from the timeline. Update selectedTimeline,
//	 * selectedTLEvent, graphics, and database.
//	 */
//	public void deleteEvent() {
//		if (selectedEvent != null && selectedTimeline != null
//				&& selectedTimeline.contains(selectedEvent)) {
//			selectedTimeline.removeEvent(selectedEvent);
//			database.removeEvent(selectedEvent, selectedTimeline.getName());
//			selectedEvent = null;
//			updateGraphics();
//		}
//	}
//
//	/**
//	 * Edit the selected event. Remove the currently selected event from the
//	 * timeline and replace it with the parameter. Update selectedTimeline,
//	 * selectedTLEvent, graphics, and database.
//	 * 
//	 * @param e
//	 *            the new event
//	 */
//	public void editEvent(TLEvent oldEvent, String title, Date startDate,
//			Date endDate, Category category, String description, Icon icon) {
//		if (selectedEvent != null && selectedTimeline != null
//				&& selectedTimeline.contains(selectedEvent)) {
//			selectedTimeline.removeEvent(selectedEvent);
//			TLEvent toAdd;
//			if (endDate != null)
//				toAdd = new Duration(title, category, startDate, endDate,
//						icon.getId(), description);
//			else
//				toAdd = new Atomic(title, category, startDate, icon.getId(),
//						description);
//			if (!icon.getName().equals("None"))
//				toAdd.setIcon(icon);
//			toAdd.setID(oldEvent.getID());
//			selectedEvent = toAdd;
//			selectedTimeline.addEvent(toAdd);
//			toAdd.setCategory(category);
//			updateGraphics();
//			database.editEvent(toAdd, selectedTimeline.getName());
//		}
//	}
//
//	/**
//	 * Update the graphics for the display screen.
//	 */
//	public void updateGraphics() {
//		graphics.clearScreen();
//		graphics.renderTimeline(selectedTimeline);
//	}
//
//	/**
//	 * Sets the mainWindow controller object
//	 * 
//	 * @param mainWindow
//	 *            the mainWindow to set
//	 */
//	public void setMainWindow(MainWindowController mainWindow) {
//		this.mainWindow = mainWindow;
//	}
//
//	/**
//	 * Gets the size of the timeline array
//	 * 
//	 * @return the size of the timelines array.
//	 */
//	public int timeSize() {
//		return timelines.size();
//	}
//
//	/**
//	 * Gets the help text
//	 * 
//	 * @return the final string help_text.
//	 */
//	public String getHelpText() {
//		return help_text;
//	}
//
//	/**
//	 * Gets the about text
//	 * 
//	 * @return the final string about_text.
//	 */
//	public String getAboutText() {
//		return about_text;
//	}
//
//	/**
//	 * Saves a category
//	 * 
//	 * @param category
//	 *            saves this category to the database.
//	 */
//	public void addCategory(Category category) {
//		database.saveCategory(category, selectedTimeline.getName());
//	}
//
//	/**
//	 * Deletes a category
//	 * 
//	 * @param category
//	 *            removes this category from the database.
//	 */
//	public void deleteCategory(Category category) {
//		database.removeCategory(category, selectedTimeline.getName());
//	}
//
//	/**
//	 * Edits a category
//	 * 
//	 * @param category
//	 *            edits this category in the database.
//	 */
//	public void editCategory(Category category) {
//		database.editCategory(category, selectedTimeline.getName());
//	}
//
//	/**
//	 * An attempt at associating the events with their icons on start-up.
//	 * Intended to load the events with their icons from the database.
//	 */
//	private void populateEventIcons() {
//		iconMap = new HashMap<Integer, Icon>();
//		for (Icon i : icons) {
//			iconMap.put(i.getId(), i);
//		}
//		for (Timeline t : timelines) {
//			if (t.getEvents() == null)
//				continue;
//			for (TLEvent e : t.getEvents()) {
//				e.setIcon(iconMap.get(e.getIconIndex()));
//				if (e.getIcon() == null)
//					e.setIcon(icons.get(0));
//			}
//		}
//	}
//
//}
