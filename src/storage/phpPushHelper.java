package storage;


import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * phpPushHelper.java
 * 
 * Updates online database with newest data from the offline database.
 * 
 * 
 * @author Conner Vick Wheaton College, CS 335, Spring 2014
 *         Project Phase 3 
 * 
 */
public class phpPushHelper {
	
	public static void send(ArrayList<Timeline> timelines) throws IOException, ParseException{
		
		JSONParser parser=new JSONParser();
		String uid = phpDBHelper.uid;
		String tName, axis_label, background_color;
		String axis_color;
		String tid, type, endDate, startDate, eName, category, description, iconid; 
		
		URL internet = new URL("http://cs.wheaton.edu/~kurt.andres/deleteTimelineEvents.php?uid="+uid);
		Scanner sc = new Scanner(internet.openStream());
		
		for(Timeline t : timelines){
			tName = t.getName();
			axis_label = t.getAxisLabel().name();
			axis_color = t.getColorTL()+""; //TODO Strings are ints
			background_color = t.getColorBG()+""; //TODO Strings are ints
			
			internet = new URL("http://cs.wheaton.edu/~kurt.andres/addTimeline.php?uid="+uid+"&name="+tName+"&axis_label="
					+axis_label+"&axis_color="+axis_color+"&background_color="+background_color);
			sc = new Scanner(internet.openStream());
			
			Object obj = parser.parse(sc.nextLine()); 
			JSONArray array = (JSONArray)obj;
			JSONObject obj1 = (JSONObject)array.get(array.size()-1);
			System.out.println(obj1.get("tid"));
			tid = (String)obj1.get("tid");
			TLEvent[] tla = t.getEvents();
			for(TLEvent someEvent: tla){
				if(someEvent instanceof Duration){
					type= "duration";
					endDate = ((Duration) someEvent).getEndDate().toString();
				}else{
					endDate = (new Date(0)).toString();
					type = "atomic";
				}
				
				eName = someEvent.getName();
				category = someEvent.getCategory().getName();
				startDate = someEvent.getStartDate().toString();
				description = someEvent.getDescription();
				iconid = Integer.toString(someEvent.getIconIndex());
				internet = new URL("http://cs.wheaton.edu/~kurt.andres/addEvent.php?tid="+tid+"&name="+eName+"&type="
						+type+"&startdate="+startDate+"&enddate="+endDate+"&category="+category+"&description="+description+"&iconid="+iconid);
				sc = new Scanner(internet.openStream());
				
			}
			
		}
		sc.close();
		
		
	
	}
}
