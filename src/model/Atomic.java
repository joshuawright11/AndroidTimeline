package model;

import java.sql.Date;

/**
 * Extension of class TLEvent to represent atomic (single date) events
 * 
 * @author Kayley Lane
 * 
 */
public class Atomic extends TLEvent {

	/**
	 * 
	 * @param name
	 *            the name of this event
	 * @param category
	 *            the category of this event
	 * @param startDate
	 *            this event's start date
	 * @param iconIndex
	 *            the integer of this icon's index for the database
	 * @param description
	 *            this event's description
	 */
	public Atomic(String name, Category category, Date startDate,
			int iconIndex, String description) {
		super(name, startDate, category, iconIndex, description);
	}

	/**
	 * Saves the event to the database. TODO: insert the functionality for
	 * saving to the database.
	 */
	public void save() {
		//
	}

}
