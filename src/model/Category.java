/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

//import javafx.scene.paint.*;
import android.graphics.Color;

/**
 * Datatype in which to categories.
 * 
 * @author Kayley
 */
public class Category {
	private String name;
	private int catColor; // GUI Color of the category.
	private int id;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of the category.
	 */
	public Category(String name) {
		this.id = -1;
		this.name = name;
//		catColor = Color.web("0x0000FF", 1.0);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of the category.
	 * @param catColor
	 *            The color of the category.
	 */
	public Category(String name, int catColor) {
		this.id = -1;
		this.name = name;
		this.catColor = catColor;
	}

	/**
	 * Returns the name of the category.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the color of the category.
	 * 
	 * @return The color.
	 */
//	public Color getColor() {
//		return catColor;
//	}

	/**
	 * Sets the name of the category.
	 * 
	 * @param name
	 *            The name to use.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the color of the category.
	 * 
	 * @param catColor
	 *            The color to use.
	 */
//	public void setColor(Color catColor) {
//		this.catColor = catColor;
//	}

	/**
	 * Saves the category. Not in use at the moment (the model saves everything).
	 */
	public void save() {

	}

	/**
	 * Sets the id of the Category
	 * 
	 * @param id the id to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Gets the id of this category
	 * 
	 * @return The id of this category
	 */
	public int getID() {
		return id;
	}
}
