package render;

import java.util.ArrayList;

import android.content.Context;
import model.Atomic;

/**
 * Atomic version of TLEventLabel
 * 
 * @author Josh Wright February 15, 2014
 */
public class AtomicTextView extends TLEventTextView {

	/**
	 * Constructor calls the super constructor with the event name, assigns
	 * instance variables, and then calls init
	 * 
	 * @param event
	 *            the event this label is associated with
	 * @param xPos
	 *            the xPosition on the screen
	 * @param yPos
	 *            the yPosition on the screen
	 * @param model
	 *            the program model
	 * @param eventLabels
	 *            the list of TLEventLabels
	 */
	public AtomicTextView(Context context, Atomic event, int xPos, int yPos) {
		super(context, xPos, yPos, event);
	}

	@Override
	public void uniqueHandlers() {
	}

	@Override
	public void uniqueDesign() {
		//setId("atomic-label"); TODO SET ATOMIC DESIGN
	}

}
