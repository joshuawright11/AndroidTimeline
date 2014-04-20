/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

//import javafx.scene.image.Image;

/**
 * 
 * @author Kayley Lane
 */
public class Icon implements Serializable{
	/**
	 * The name of the image, seperate from its entire path.
	 */
	private String name;

	/**
	 * The image associated with this icon.
	 */
	private Drawable icon;

	/**
	 * The id of this icon
	 */
	private int id;

	/**
	 * The file path of this icon
	 */
	private String path;

	/**
	 * The Constructor
	 * 
	 * @param name
	 *            The name to set
	 * @param icon
	 *            The icon to set
	 * @param path
	 *            The path to set
	 */
	public Icon(String name, Drawable icon, String path) {
		this.name = name;
		this.icon = icon;
		this.path = path;
		this.setId(-1);
	}

	/**
	 * Get the name of the icon.
	 * 
	 * @return The name of the icon
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the image of the icon
	 * 
	 * @return The image of the icon
	 */
	public Drawable getImage() {
		return icon;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the path of the icon
	 * 
	 * @return The path of the icon
	 */
	public String getPath() {
		return path;
	}

}
