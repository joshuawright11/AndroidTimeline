/**
 * 
 */
package storage;

import java.util.ArrayList;
import java.util.HashMap;

import model.Category;
import model.Icon;
import model.TLEvent;
import model.Timeline;

/**
 * This is the interface for DBHelper. This class is used to access a database
 * with the intent of storing and retrieving timelines. It also stores all
 * datatypes such as categories and icons associated with the TimelineMaker as
 * well. Anything stored in the database must have a unique id in its class,
 * which gets set when it is pulled from the database. This way we can edit all
 * fields of an object in the databse (the _id column is unique and
 * autoincremented so it can't be cheanged.)
 * 
 * The table structure is:
 * 
 * timeline_info contains info about timelines (color, axisUnit, etc)
 * timeline_categories contains category names and what timeline they are
 * associated with timeline_icons contains all icons with
 * 
 * one table for each timeline (called that timeline's name) with all its events
 * and their info in that table
 * 
 * @author Josh Wright 
 * Created: Jan 29, 2014 
 * Edited: March 7, 2014 
 * Package: storage
 */

public interface DBHelperAPI {

	/**
	 * Saves a timeline and all its information to the database
	 * 
	 * @param timeline
	 *            the timeline to write
	 * @return
	 */
	public boolean saveTimeline(Timeline timeline);

	/**
	 * Removes a timeline and all its information from the database
	 * 
	 * @param timeline
	 *            the timeline to remove
	 * @return
	 */
	public boolean removeTimeline(Timeline timeline);

	/**
	 * Swaps a timeline for another timeline. Used for updating a timeline's
	 * information.
	 * 
	 * @param oldTimeline
	 *            the old timeline to remove
	 * @param newTimeline
	 *            the new timeline to switch it with
	 * @return the newly saved timeline
	 */
	public boolean editTimelineInfo(Timeline timeline);

	/**
	 * Saves an event to the table of its timeline.
	 * 
	 * @param event
	 *            The event to save
	 */
	public void saveEvent(TLEvent event, String timelineName);

	/**
	 * Removes an event from its timeline's table.
	 * 
	 * @param event
	 *            The event to remove
	 * @return false if the event did not exist in the database
	 */
	public boolean removeEvent(TLEvent event, String timelineName);

	/**
	 * Edits the details of an event, based on its unique id
	 * 
	 * @param event
	 *            The event to edit
	 * @param timelineName
	 *            The name of the timeline that event is associated with
	 * @return whether the event was successfully edited
	 */
	public boolean editEvent(TLEvent event, String timelineName);

	/**
	 * Returns a HashMap of category to timeline names
	 * 
	 * @return The HashMap
	 */
	public HashMap<Category, String> getCategories();

	/**
	 * Saves a category to the database based on unique id
	 * 
	 * @param category
	 *            The category to save
	 * @param timelineName
	 *            The name of the timeline this category is associated with
	 */
	public void saveCategory(Category category, String timelineName);

	/**
	 * Removes a category from the database based on unique id
	 * 
	 * @param category
	 *            The category to remove
	 * @param timelineName
	 *            The name of the timeline this category is associated with
	 * @return Whether the category was successfully deleted
	 */
	public boolean removeCategory(Category category, String timelineName);

	/**
	 * Edits a category in the database, based off of unique id
	 * 
	 * @param category
	 *            The category to edit
	 * @param timelineName
	 *            The name of the timeline this category is associated with
	 * @return Whether the category was successfully edited
	 */
	public boolean editCategory(Category category, String timelineName);

	/**
	 * Saves an Icon to the database (the image and name)
	 * 
	 * @param icon
	 *            The icon to save
	 */
	public void saveIcon(Icon icon);

	/**
	 * Removed an icon from the database
	 * 
	 * @param icon
	 *            The icon to remove
	 * @return Whether the icon was successfully removed
	 */
	public boolean removeIcon(Icon icon);

	/**
	 * Gets an ArrayList of all icons from the database
	 * 
	 * @return ArrayList of Icons
	 */
	public ArrayList<Icon> getIcons();

	/**
	 * Returns an array of all timelines currently in the database. The
	 * timelines have their events and any additional info built into them
	 * 
	 * @return array of all timelines in the database
	 */
	public Timeline[] getTimelines();
}
