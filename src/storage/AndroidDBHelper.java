package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import model.*;
import model.Timeline.AxisLabel;

/**
 * 
 * @author Josh Wright 
 * Created: Jan 29, 2014 
 * Updated: March 1, 2014 
 * Package: storage
 * 
 *         Using SQL ideas and very minimal code from
 *         http://www.tutorialspoint.com/jdbc/jdbc-sample-code.htm
 * 
 */
public class AndroidDBHelper implements DBHelperAPI {
	
	
	private DBOpenHelper mDbHelper;
	private SQLiteDatabase database;
	
	/**
	 * Used for making database Android compatible, useful to save as a variable
	 */
	private static final String ID = "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE"; // unique id

	private Context context;
	
	/**
	 * Constructor; sets the database name
	 * 
	 * @param dbName
	 *            the path to the database
	 */
	public AndroidDBHelper(Context context) {
		this.context = context;
		init();
	}

	/**
	 * Initializes the timeline_info database. This can store various timeline
	 * attributes though currently only has 1, axisLabel
	 * 
	 */
	private void init() {
		open();
		try {
			database.execSQL("CREATE TABLE timeline_info (" + ID
					+ ", timelineName TEXT, axisLabel TEXT, "
					+ "backgroundColor TEXT, axisColor TEXT);");
		} catch (SQLException e) {
			if (e.getMessage().contains("already exists")) {
				// it has already been created, no issues
			} else
				e.printStackTrace();
		}
		try {
			database.execSQL("CREATE TABLE timeline_categories (" + ID
					+ ", categoryName TEXT, timelineName TEXT, color TEXT);");
		} catch (SQLException e) {
			if (e.getMessage().contains("already exists")) {
				// it has already been created, no issues
			} else
				e.printStackTrace();
		}
		try {
			database.execSQL("CREATE TABLE timeline_icons (" + ID
					+ ", iconName TEXT, icon BLOB);");
		} catch (SQLException e) {
			if (e.getMessage().contains("already exists")) {
				// it has already been created, no issues
			} else
				e.printStackTrace();
		}
		close();
	}

	/**
	 * Opens the connection to the database. This gets called before and
	 * database queries are made It sets up the connection and statement
	 * variables.
	 * 
	 */
	private void open() {
		mDbHelper = new DBOpenHelper(context);
		database = mDbHelper.getWritableDatabase();
		
	}

	/**
	 * Closes the database. This must be called whenever the user is done using
	 * the database or else the database will be locked. Ensure that this is
	 * called when finished accessing database.
	 * 
	 */
	private void close() {
		mDbHelper.close();
		database.close();
	}

	@Override
	public Timeline[] getTimelines() { // this could probably be split up into
										// methods
		open();
		try {
			Cursor results = database
					.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' "
							+ "and name != \"sqlite_sequence\" and name != \"timeline_info\" and name != \"timeline_categories\" "
							+ "and name != \"timeline_icons\"  and name != \"android_metadata\";", new String[]{});
			ArrayList<String> timelineNames = new ArrayList<String>();
			int numTimelines = 0;
			System.out.println(results.getCount());
			if(results.moveToFirst()){
				while (!results.isAfterLast()) { // Get all timeline names
					
					System.out.println("DBHLP138 IN LOOP!");
					
					numTimelines++;
					timelineNames.add(results.getString(0));
					results.moveToNext();
				}
			}
			Timeline[] timelines = new Timeline[numTimelines];
			for (int j = 0; j < numTimelines; j++) { // Get all timelines event
														// arrays
				results = database.rawQuery("select * from "
						+ timelineNames.get(j) + ";", null);
				ArrayList<TLEvent> events = new ArrayList<TLEvent>();
				if(results.moveToFirst()){
					while (!results.isAfterLast()) { // Get all events for the event
						String name = results.getString(results.getColumnIndex("eventName"));
						String type = results.getString(results.getColumnIndex("type"));
						TLEvent event = null;
						if (type.equals("atomic")) {
							String cat = results.getString(results.getColumnIndex("category"));
							Category category = new Category(cat);
							Date startDate = new Date(results.getLong(results.getColumnIndex("startDate")));
							int iconIndex = results.getInt(results.getColumnIndex("icon"));
							String description = results.getString(results.getColumnIndex("description"));
							event = new Atomic(name, category, startDate,
									iconIndex, description);
						} else if (type.equals("duration")) {
							String cat = results.getString(results.getColumnIndex("category"));
							Category category = new Category(cat);
							Date startDate = new Date(results.getLong(results.getColumnIndex("startDate")));
							Date endDate = new Date(results.getLong(results.getColumnIndex("endDate")));
							int iconIndex = results.getInt(results.getColumnIndex("icon"));
							String description = results.getString(results.getColumnIndex("description"));
							event = new Duration(name, category, startDate,
									endDate, iconIndex, description);
						} else {
							System.out.println("YOU DONE MESSED UP.");
						}
						setEventID(event, timelineNames.get(j));
						events.add(event);
					}
				}
				Timeline timeline = new Timeline(timelineNames.get(j),
						events.toArray(new TLEvent[events.size()]), Color.BLUE,
						Color.GRAY, AxisLabel.YEARS);
				setTimelineID(timeline);
				AxisLabel label = AxisLabel.values()[getAxisLabel(timeline)];
				int backgroundColor = getBackgroundColor(timeline.getID());
				int axisColor = getAxisColor(timeline.getID());
				timeline.setAxisLabel(label);
				timeline.setColorBG(backgroundColor);
				timeline.setColorTL(axisColor);
				timelines[j] = timeline;
			}
			close();
			return timelines;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return null;
	}

	@Override
	public boolean saveTimeline(Timeline timeline) {
		String tlName = timeline.getName();
		open();
		try {
			database.execSQL("CREATE TABLE "
							+ tlName
							+ " ("
							+ ID
							+ ",eventName TEXT, type TEXT, startDate DATETIME, endDate DATETIME, "
							+ "category TEXT, icon INTEGER, description TEXT);");
			writeTimelineInfo(timeline);
			setTimelineID(timeline);

		} catch (SQLException e) {
			if (e.getMessage().contains("already exists")) {
				System.out.println("A timeline with that name already exists!");
				close();
				return false;
			}
			e.printStackTrace();
		}
		close();
		
		Log.d("226", "A TIMELINE HAS BEEN SAVED! "+timeline.getName());
		
		if (timeline.getEvents() == null) {
			return true; // did not save any events, timeline still created
		}
		open();
		for (TLEvent event : timeline.getEvents()) {
			try {
				if (event instanceof Atomic) {
					writeEvent((Atomic) event, tlName);
				} else if (event instanceof Duration) {
					writeEvent((Duration) event, tlName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("Nothing!");
			}
		}
		close();
		return true;
	}
	
	@Override
	public boolean removeTimeline(Timeline timeline) {
//		open();
//		try {
//			statement.executeUpdate("DROP TABLE IF EXISTS'"
//					+ timeline.getName() + "';"); // if exists is probably bad
//													// for this
//			removeTimelineInfo(timeline.getID());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		close();
		return true;
	}

	@Override
	public boolean editTimelineInfo(Timeline timeline) {
//		open();
//		try {
//			changeTimelineName(timeline);
//			updateTimelineInfo(timeline);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			close();
//			return false;
//		}
//		close();
		return true;
	}

//	/**
//	 * Gets the name of a timeline, based on its unique id
//	 * 
//	 * @param timeline
//	 *            the timeline whose name will be fetched
//	 * @return the name of the timeline
//	 * @throws SQLException
//	 *             because there are databases
//	 */
//	private String getName(Timeline timeline) throws SQLException {
//		String SELECT_LABEL = "SELECT timelineName FROM timeline_info WHERE _id = ?;";
//		PreparedStatement pstmt = connection.prepareStatement(SELECT_LABEL);
//		pstmt.setInt(1, timeline.getID());
//		resultSet = pstmt.executeQuery();
//		String oldName = resultSet.getString(1);
//		return oldName;
//	}

//	/**
//	 * Changes the name of a timeline based on unique id
//	 * 
//	 * @param timeline
//	 *            the timeline whose name will be changed
//	 * @throws SQLException
//	 *             because there are databases
//	 */
//	private void changeTimelineName(Timeline timeline) throws SQLException {
//		String oldName = getName(timeline);
//		String newName = timeline.getName();
//		if (oldName.equals(newName))
//			return;
//		String SELECT_LABEL = "ALTER TABLE \"" + oldName + "\" RENAME TO \""
//				+ newName + "\";";
//		PreparedStatement pstmt = connection.prepareStatement(SELECT_LABEL);
//		pstmt.execute();
//	}

	/**
	 * Uses prepared statements to insert the timelineName and axisLabel into
	 * the timeline_info table
	 * 
	 * @param timelineName
	 *            the timeline of the axisLabel to write
	 * @param axisLabel
	 *            the axisLabel enum value
	 * @throws SQLException
	 *             because there are databases
	 */
	private void writeTimelineInfo(Timeline timeline) throws SQLException {
		String INSERT_LABEL = "INSERT INTO timeline_info (timelineName, axisLabel, backgroundColor, axisColor) VALUES "
				+ "(?,?,?,?);";
		SQLiteStatement stmt = database.compileStatement(INSERT_LABEL);
		stmt.bindString(1, timeline.getName());
		stmt.bindString(2, timeline.getAxisLabel().name());
		stmt.bindString(3, "0x"+(Long.toHexString(timeline.getColorBG())));
		stmt.bindString(4, "0x"+(Long.toHexString(timeline.getColorTL())));
		stmt.executeInsert();
	}

//	/**
//	 * Syncs the database to this timeline's info. Uses id to access the
//	 * timeline in the database
//	 * 
//	 * 
//	 * @param timeline
//	 *            the timeline to update
//	 * @throws SQLException
//	 *             because there are databases
//	 */
//	private void updateTimelineInfo(Timeline timeline) throws SQLException {
//		throw new SQLException();
//		String UPDATE_NAME_LABEL = " UPDATE timeline_info SET timelineName=? WHERE _id=?;";
//		PreparedStatement pstmt = connection
//				.prepareStatement(UPDATE_NAME_LABEL);
//		pstmt.setString(1, timeline.getName());
//		pstmt.setInt(2, timeline.getID());
//		pstmt.executeUpdate();
//	
//		String UPDATE_AXIS_LABEL = " UPDATE timeline_info SET axisLabel=? WHERE _id=?;";
//		pstmt = connection.prepareStatement(UPDATE_AXIS_LABEL);
//		pstmt.setString(1, timeline.getAxisLabel().name());
//		pstmt.setInt(2, timeline.getID());
//		pstmt.executeUpdate();
//	
//		String UPDATE_BG_LABEL = " UPDATE timeline_info SET backgroundColor=? WHERE _id=?;";
//		pstmt = connection.prepareStatement(UPDATE_BG_LABEL);
//		pstmt.setString(1, timeline.getColorBG()+"");
//		pstmt.setInt(2, timeline.getID());
//		pstmt.executeUpdate();
//	
//		String UPDATE_AXISCOLOR_LABEL = " UPDATE timeline_info SET axisColor=? WHERE _id=?;";
//		pstmt = connection.prepareStatement(UPDATE_AXISCOLOR_LABEL);
//		pstmt.setString(1, timeline.getColorTL()+"");
//		pstmt.setInt(2, timeline.getID());
//		pstmt.executeUpdate();
//	}

	/**
	 * Sets a timeline's id field to what its unique id in the timeline_info
	 * table is. Must call this immediately after the timeline is first stored
	 * in the database.
	 * 
	 * @param timeline
	 *            the timeline whose id field is set
	 */
	private void setTimelineID(Timeline timeline) throws SQLException {
		String SELECT_LABEL = "SELECT _id FROM timeline_info WHERE timelineName = ?;";
		Cursor c = database.rawQuery(SELECT_LABEL, new String[] {timeline.getName()});
		c.moveToFirst();
		Log.d("387", "Setting the ID for timeline "+timeline.getName());
		int id = c.getInt(0);
		timeline.setID(id);
	}

//	/**
//	 * Uses prepared statements to remove the timeline's info from the
//	 * timeline_info table
//	 * 
//	 * @param id
//	 *            the id of the timeline to remove the info of
//	 * @throws SQLException
//	 *             because there are databases
//	 */
//	private void removeTimelineInfo(int id) throws SQLException {
//		String REMOVE_LABEL = "DELETE FROM timeline_info WHERE _id = ?;";
//		PreparedStatement pstmt = connection.prepareStatement(REMOVE_LABEL);
//		pstmt.setInt(1, id);
//		pstmt.executeUpdate();
//	}

	/**
	 * @param timelineName
	 *            the name of the timeline to get the axisLabel of
	 * @return the index of the AxisLabel (there is room for more)
	 * @throws SQLException
	 *             because there are databases
	 */
	private int getAxisLabel(Timeline timeline) throws SQLException {
		String SELECT_LABEL = "SELECT axisLabel FROM timeline_info WHERE _id = ?;";
		
		Cursor c = database.rawQuery(SELECT_LABEL, new String[] {timeline.getID()+""}); 
		c.moveToFirst();

		String labelName = c.getString(0);
		
		if (labelName.equals("DAYS")) {
			return 0;
		}else if (labelName.equals("MONTHS")) {
			return 2;
		}else if (labelName.equals("YEARS")) {
			return 3;
		}else
			return 3;
	}
	

	/**
	 * Gets the axis color of a timeline from the database based on its unique
	 * id
	 * 
	 * @param id
	 *            the id of the timeline whose axis color will be fetched
	 * @return the Color
	 * @throws SQLException
	 *             because there are databases
	 */
	private int getAxisColor(int id) throws SQLException {
		String SELECT_LABEL = "SELECT axisColor FROM timeline_info WHERE _id = ?;";
		
		Cursor c = database.rawQuery(SELECT_LABEL, new String[] {id+""}); 
		c.moveToFirst();
		String color = c.getString(0);
		int toReturn = Color.BLUE;
		return toReturn;
	}

	/**
	 * Gets the background color of a timeline from the database based on its
	 * unique id
	 * 
	 * @param id
	 *            the id of the timeline whose background color will be fetched
	 * @return the Color
	 * @throws SQLException
	 *             because there are databases
	 */
	private int getBackgroundColor(int id) throws SQLException {
		String SELECT_LABEL = "SELECT backgroundColor FROM timeline_info WHERE _id = ?;";
		Cursor c = database.rawQuery(SELECT_LABEL, new String[] {id+""}); 
		c.moveToFirst();
		String color = c.getString(0);
		String colorFormat = "0x"+(Integer.toHexString(Integer.parseInt(color)));
		Log.d("getBackgroundColor", colorFormat);
		int toReturn = Color.parseColor(colorFormat.replaceFirst("^0x", "#")); // Hex to Android color
		return toReturn;
	}

	@Override
	public void saveEvent(TLEvent event, String timelineName) {
		open();
		try {
			if (event instanceof Atomic)
				writeEvent((Atomic) event, timelineName);
			else if (event instanceof Duration)
				writeEvent((Duration) event, timelineName);
			setEventID(event, timelineName);
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
		close();
	}

	@Override
	public boolean removeEvent(TLEvent event, String timelineName) {
//		open();
//		try {
//			String REMOVE_EVENT_LABEL = "DELETE FROM " + timelineName
//					+ " WHERE _id = ?;";
//			PreparedStatement pstmt = connection
//					.prepareStatement(REMOVE_EVENT_LABEL);
//			pstmt.setInt(1, event.getID());
//			pstmt.executeUpdate();
//			close();
//			return true;
//		} catch (SQLException sql) {
//			close();
//			return false;
//		}
		return false;
	}

	@Override
	public boolean editEvent(TLEvent event, String timelineName) {
//		open();
//		try {
//			String UPDATE_NAME_LABEL = " UPDATE " + timelineName
//					+ " SET eventName=? WHERE _id=?;";
//			PreparedStatement pstmt = connection
//					.prepareStatement(UPDATE_NAME_LABEL);
//			pstmt.setString(1, event.getName());
//			pstmt.setInt(2, event.getID());
//			pstmt.executeUpdate();
//
//			String UPDATE_STARTDATE_LABEL = " UPDATE " + timelineName
//					+ " SET startDate=? WHERE _id=?;";
//			pstmt = connection.prepareStatement(UPDATE_STARTDATE_LABEL);
//			pstmt.setDate(1, event.getStartDate());
//			pstmt.setInt(2, event.getID());
//			pstmt.executeUpdate();
//
//			if (event instanceof Duration) {
//				String UPDATE_ENDDATE_LABEL = " UPDATE " + timelineName
//						+ " SET endDate=? WHERE _id=?;";
//				pstmt = connection.prepareStatement(UPDATE_ENDDATE_LABEL);
//				pstmt.setDate(1, ((Duration) event).getEndDate());
//				pstmt.setInt(2, event.getID());
//				pstmt.executeUpdate();
//			}
//
//			String UPDATE_CATEGORY_LABEL = " UPDATE " + timelineName
//					+ " SET category=? WHERE _id=?;";
//			pstmt = connection.prepareStatement(UPDATE_CATEGORY_LABEL);
//			pstmt.setString(1, event.getCategory().getName());
//			pstmt.setInt(2, event.getID());
//			pstmt.executeUpdate();
//
//			String UPDATE_ICON_LABEL = " UPDATE " + timelineName
//					+ " SET icon=? WHERE _id=?;";
//			pstmt = connection.prepareStatement(UPDATE_ICON_LABEL);
//			pstmt.setInt(1, event.getIcon().getId());
//			pstmt.setInt(2, event.getID());
//			pstmt.executeUpdate();
//
//			String UPDATE_DESCRIPTION_LABEL = " UPDATE " + timelineName
//					+ " SET description=? WHERE _id=?;";
//			pstmt = connection.prepareStatement(UPDATE_DESCRIPTION_LABEL);
//			pstmt.setString(1, event.getDescription());
//			pstmt.setInt(2, event.getID());
//			pstmt.executeUpdate();
//
//			close();
//			return true;
//		} catch (SQLException sql) {
//			sql.printStackTrace();
//			close();
//			return false;
//		}
		return false;
	}

	/**
	 * Helper method for writeTimeline. Puts the duration event in the correct
	 * timeline's database using prepared statements; overloaded see above
	 * 
	 * @param event
	 *            the duration event to insert
	 * @param tlName
	 *            the name of the timeline whose table this event belongs in
	 * @throws SQLException
	 *             because there are databases
	 */
	private void writeEvent(Duration event, String tlName) throws SQLException {
		String INSERT_DURATION = "INSERT INTO "
				+ tlName
				+ " (eventName,type,startDate,endDate,category, icon, description) VALUES "
				+ "(?,?,?,?,?,?,?);";
		
		SQLiteStatement stmt = database.compileStatement(INSERT_DURATION);
		stmt.bindString(1, event.getName());
		stmt.bindString(2, "duration");
		stmt.bindLong(3, event.getStartDate().getTime());
		stmt.bindLong(4, event.getEndDate().getTime());
		stmt.bindString(5, event.getCategory().getName());
		stmt.bindString(6, event.getIcon().getId()+"");
		stmt.bindString(7, event.getDescription());
		stmt.executeInsert();
	}

	/**
	 * Helper method for writeTimeline. Puts the atomic event in the correct
	 * timeline's database using prepared statements; overloaded see below
	 * 
	 * @param event
	 *            the atomic event to insert
	 * @param tlName
	 *            the name of the timeline whose table this event belongs in
	 * @throws SQLException
	 *             because there are databases
	 */
	private void writeEvent(Atomic event, String tlName) throws SQLException {
		String INSERT_ATOMIC = "INSERT INTO "
				+ tlName
				+ " (eventName,type,startDate,endDate,category, icon, description) VALUES "
				+ "(?,?,?,NULL,?,?,?);";
		SQLiteStatement stmt = database.compileStatement(INSERT_ATOMIC);
		stmt.bindString(1, event.getName());
		stmt.bindString(2, "duration");
		stmt.bindLong(3, event.getStartDate().getTime());
		stmt.bindString(4, event.getCategory().getName());
		stmt.bindString(5, event.getIcon().getId()+"");
		stmt.bindString(6, event.getDescription());
		stmt.executeInsert();
	}

	/**
	 * Gets the unique id for a timeline from the timeline_info table, based on
	 * the timeline's name. Must set this immediately after the timeline is
	 * first stored in the database.
	 * 
	 * @param name
	 *            the name of the timeline whose id will be returned
	 * @return the id of the timeline
	 * @throws SQLException
	 *             because there are databases.
	 */
	private void setEventID(TLEvent event, String timelineName)
			throws SQLException {
		String SELECT_LABEL = "SELECT _id FROM " + timelineName
				+ " WHERE eventName = ?;";
		Cursor c = database.rawQuery(SELECT_LABEL, new String[] {event.getName()}); 
		c.moveToFirst();
		int id = c.getInt(0);
		event.setID(id);
	}

	@Override
	public HashMap<Category, String> getCategories() {
		open();
		try {
			Cursor c = database
					.rawQuery("SELECT * FROM timeline_categories;", new String[]{});
			HashMap<Category, String> categories = new HashMap<Category, String>();
			while (c.moveToNext()) { // Get all category info
				int id = c.getInt(0);
				String name = c.getString(c.getColumnIndex("categoryName"));
				String timelineName = c.getString(c.getColumnIndex("timelineName"));
				int color = Color.BLUE; // Hex to Android color
				Category category = new Category(name, color);
				category.setID(id);
				categories.put(category, timelineName);
			}
			close();
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return null;
	}

	@Override
	public void saveCategory(Category category, String timelineName) {
		String INSERT_CATEGORY = "INSERT INTO timeline_categories (categoryName,timelineName,color) VALUES (?,?,?);";
		open();
		try {
			SQLiteStatement stmt = database.compileStatement(INSERT_CATEGORY);
			stmt.bindString(1, category.getName());
			stmt.bindString(2, timelineName);
			stmt.bindString(3, category.getColor()+""); //TODO COLOR MIGHT BE WRONG
			stmt.executeInsert();
			setCategoryID(category, timelineName);
		} catch (SQLException e) {
			// Already exists?
			e.printStackTrace();
		}
		close();
	}

	@Override
	public boolean removeCategory(Category category, String timelineName) {
//		open();
//		try {
//			String REMOVE_CATEGORY_LABEL = "DELETE FROM timeline_categories WHERE _id = ? and timelineName = ?;";
//			PreparedStatement pstmt = connection
//					.prepareStatement(REMOVE_CATEGORY_LABEL);
//			pstmt.setInt(1, category.getID());
//			pstmt.setString(2, timelineName);
//			pstmt.executeUpdate();
//			close();
//			return true;
//		} catch (SQLException sql) {
//			close();
//			return false;
//		}
		return false;
	}

	@Override
	public boolean editCategory(Category category, String timelineName) {
//		open();
//		try {
//			String UPDATE_NAME_LABEL = " UPDATE timeline_categories SET categoryName=? WHERE _id=?;";
//			PreparedStatement pstmt2 = connection
//					.prepareStatement(UPDATE_NAME_LABEL);
//			pstmt2.setString(1, category.getName());
//			pstmt2.setInt(2, category.getID());
//			pstmt2.executeUpdate();
//	
//			String UPDATE_COLOR_LABEL = " UPDATE timeline_categories SET color=? WHERE _id=?;";
//			pstmt2 = connection.prepareStatement(UPDATE_COLOR_LABEL);
//			pstmt2.setString(1, category.getColor()+"");
//			pstmt2.setInt(2, category.getID());
//			pstmt2.executeUpdate();
//	
//			close();
//			return true;
//		} catch (SQLException sql) {
//			sql.printStackTrace();
//			close();
//			return false;
//		}
		return false;
	}

	/**
	 * Sets the id of a category based on it's database unique id
	 * 
	 * @param category
	 *            the category whose id will be set
	 * @throws SQLException
	 *             because there are databases
	 */
	private void setCategoryID(Category category, String timelineName)
			throws SQLException {
		String SELECT_LABEL = "SELECT _id FROM timeline_categories WHERE categoryName = ? and timelineName = ?;";
		Cursor results = database.rawQuery(SELECT_LABEL, new String[]{category.getName(), timelineName});
		results.moveToFirst();
		int id = results.getInt(0);
		category.setID(id);
	}

	@Override
	public ArrayList<Icon> getIcons() {
//		open();
//		try {
//			ResultSet resultSet2 = statement
//					.executeQuery("SELECT * FROM timeline_icons;");
//			ArrayList<Icon> icons = new ArrayList<Icon>();
//			while (resultSet2.next()) { // Get all category info
//				int id = resultSet2.getInt(1);
//				String name = resultSet2.getString("iconName");
//				InputStream is = resultSet2.getBinaryStream("icon");
//				Icon icon = new Icon(name, Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is),20,20,false), ""); // path is no longer necessary
//				icon.setId(id);
//				icons.add(icon);
//			}
//			close();
//			return icons;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		close();
		return null;	
	}

	@Override
	public void saveIcon(Icon icon) {
//		String INSERT_ICON = "INSERT INTO timeline_icons (iconName,icon) VALUES (?,?);";
//		open();
//		try {
//			PreparedStatement pstmt = connection.prepareStatement(INSERT_ICON);
//			File f = new File(icon.getPath());
//			FileInputStream fin = new FileInputStream(f);
//			pstmt.setString(1, icon.getName());
//			pstmt.setBinaryStream(2, fin, (int) f.length());
//			pstmt.executeUpdate();
//			setIconID(icon);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			System.out.println("That file can't be found!");
//			e.printStackTrace();
//		}
//		close();
	}

	@Override
	public boolean removeIcon(Icon icon) {
//		open();
//		try {
//			String REMOVE_ICON_LABEL = "DELETE FROM timeline_icons WHERE _id = ?;";
//			PreparedStatement pstmt = connection
//					.prepareStatement(REMOVE_ICON_LABEL);
//			pstmt.setInt(1, icon.getId());
//			pstmt.executeUpdate();
//			close();
//			return true;
//		} catch (SQLException sql) {
//			close();
//			return false;
//		}
		return false;
	}

//	/**
//	 * Sets the unique id of an icon based on its database unique id
//	 * 
//	 * @param icon
//	 *            the icon whose id will be set
//	 */
//	private void setIconID(Icon icon) throws SQLException {
//		String SELECT_LABEL = "SELECT _id FROM timeline_icons WHERE iconName = ?;";
//		PreparedStatement pstmt = connection.prepareStatement(SELECT_LABEL);
//		pstmt.setString(1, icon.getName());
//		ResultSet resultSet2 = pstmt.executeQuery();
//		int id = resultSet2.getInt(1);
//		icon.setId(id);
//	}

}
