/**
 * 
 */
package model;

import java.sql.Date;

import storage.DBHelper;

public class TLEvent {
	protected String description, name;

	/**
	 * The start date of this event
	 */
	private Date startDate;

	/**
	 * The category of this event
	 */
	private Category category;

	/**
	 * the unique id of this event
	 */
	private int id;

	/**
	 * The icon of this event
	 */
	private Icon icon;

	/**
	 * The index of this event's icon in the icon hashmap
	 */
	private int iconIndex;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of the Event to be constructed.
	 */
	public TLEvent(String name) {
		this.setIconIndex(-1);
		this.name = name;
		category = new Category("Default");
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of the Event to be consructed.
	 * @param startDate
	 *            The int value of start of the event.
	 * @param category
	 *            The category of the event.
	 * @param iconIndex
	 *            The index to set
	 * @param description
	 *            The description of this event
	 */
	public TLEvent(String name, Date startDate, Category category,
			int iconIndex, String description) {
		this.name = name;
		this.startDate = startDate;
		this.category = category;
		this.description = description;
//		this.icon = new Icon("None", null, null); // TODO this is kludgy
		this.iconIndex = iconIndex;
	}

	/**
	 * Method to return the description of the TLEvent.
	 * 
	 * @return The Description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Method to return the name of the TLEvent.
	 * 
	 * @return The name of the event.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method to return the start date of the TLEvent.
	 * 
	 * @return The start date.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Method to return the category of the TLEvent.
	 * 
	 * @return The category.
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            The description to use.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to use.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate
	 *            The start date to use.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            The category to use.
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * Saves the event to the database.
	 */
	public void save(DBHelper db, String timelineName) {
		db.saveEvent(this, timelineName);
	}

	/**
	 * Getter method for the id
	 * 
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	/**
	 * Setter method for the id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Getter method for the icon
	 * 
	 * @return the icon associated with this event
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * Setter method for the icon
	 * 
	 * @param icon
	 *            sets the icon of this event
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	/**
	 * Getter method for the iconIndex
	 * 
	 * @return the iconIndex
	 */
	public int getIconIndex() {
		return iconIndex;
	}

	/**
	 * Setter method for the iconIndex
	 * 
	 * @param iconIndex
	 *            the iconIndex to set
	 */
	public void setIconIndex(int iconIndex) {
		this.iconIndex = iconIndex;
	}
}