package com.jd.android.quakereport;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by jd158 on 25/09/2016.
 */

public class EarthquakeAdapter extends ArrayAdapter{

    private static final String LOCATION_SEPARATOR = " of ";
    String locationOffset;
    String primaryLocation;
    //We initialize the ArrayAdapter's internal storage for the context and the list.
    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquaker_list_item, parent, false);
        }

        // Get the {@link Earthquake} object located at this position in the list
        Earthquake currentEarthquake = (Earthquake) getItem(position);

        // Find the ImageView in the list_item.xml layout with the ID version_name
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        // set this text on the name TextView
        String formattedMag = formatMagnitude(currentEarthquake.getMagnitude());

        magnitudeView.setText(formattedMag);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        // take the background and save on magnitudeCircle variable
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Create a new String object from the String Place
        String locationObject = new String(currentEarthquake.getLocation());

        // Format the location String (i.e. "88km N of    Yelizovo, Russia")
        //String formattedDistance = formatLocationDistance(locationObject);

        if (locationObject.contains(LOCATION_SEPARATOR)) {
            String[] parts = locationObject.split(LOCATION_SEPARATOR);
            primaryLocation = parts[0] + LOCATION_SEPARATOR; //
            locationOffset = parts[1]; //
            //String formattedDistance = part1;
        }
            else {
            primaryLocation = locationObject;
            locationOffset = getContext().getString(R.string.near_the);
        }
        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView distanceView = (TextView) listItemView.findViewById(R.id.distance);

        // Display the Distance of the current earthquake in that TextView
        distanceView.setText(primaryLocation);

        // Find the ImageView in the list_item.xml layout with the ID version_name
        TextView cityView = (TextView) listItemView.findViewById(R.id.city);

        // Display the City of the current earthquake
        cityView.setText(locationOffset);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        // Find the ImageView in the list_item.xml layout with the ID version_name
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);

        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date timeObject = new Date(currentEarthquake.getTimeInMilliseconds());

        // Find the ImageView in the list_item.xml layout with the ID version_name
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);

        // Format the date string (i.e. "Mar 3, 1984")
        String formattedTime = formatTime(timeObject);

        // Display the date of the current earthquake in that TextView
        timeView.setText(formattedTime);

        return listItemView;
    }

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){

        int color = 0;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor){
            case 1:
                color = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 2:
                color = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 3:
                color = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 4:
                color = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 5:
                color = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 6:
                color = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            case 7:
                color = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 8:
                color = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 9:
                color = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            case 10:
                color = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                break;
        }
        return color;
    }
}
