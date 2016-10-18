package com.jd.android.quakereport;

/**
 * Created by jd158 on 25/09/2016.
 */
public class Earthquake {

    // Magnitude of Earthquake
    private double mMagnitude;

    // Location of Earthquake
    private String mLocation;

    /** Time of the earthquake */
    private long mTimeInMilliseconds;

    // URL of Earthquake page
    private String mUrl;

    public Earthquake(double Magnitude, String Location, long TimeInMilliseconds, String Url)  {
        mMagnitude = Magnitude;
        mLocation = Location;
        mTimeInMilliseconds = TimeInMilliseconds;
        mUrl = Url;
    }

    public double getMagnitude(){
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getUrl() {
        return mUrl;
    }
}
