package com.jd.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Sample JSON response for a USGS query
     */
    private static final String SAMPLE_JSON_RESPONSE = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private static final String LOG_TAG = null;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<com.jd.android.quakereport.Earthquake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = null;

        try {
            url = createUrl(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        // Extract relevant fields from the JSON response and create an {@link Book} object
        //ArrayList book = extractFeatureFromJson(jsonResponse);
        List<com.jd.android.quakereport.Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return the {@link Book} object as the result fo the {@link TsunamiAsyncTask}
        //return books;
        return earthquakes;
    }
    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     * @param earthquakeJSON
     */
    public static List<com.jd.android.quakereport.Earthquake> extractFeatureFromJson(String earthquakeJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<com.jd.android.quakereport.Earthquake> earthquakes = new ArrayList<>();

        String data = "";
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            //Extract “features” JSONArray
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            int i;

            Date dateObject;
            //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD,yyyy h:mm a", Locale.ENGLISH);
            // Loop through each feature in the array
            for (i = 0; i < earthquakeArray.length(); i++) {
                //Get earthquake JSONObject at position i

                JSONObject jsonObject = earthquakeArray.getJSONObject(i);
                //Get “properties” JSONObject
                JSONObject properties = jsonObject.getJSONObject("properties");

                //Extract “mag” for magnitude
                double mag = properties.getDouble("mag");

                //Extract “place” for location
                String place = properties.getString("place").toString();

                String Url = properties.getString("url").toString();

                // Extract “time” for time
                long time = properties.getLong("time");
                dateObject = new Date(time);

                com.jd.android.quakereport.Earthquake earthquake = new com.jd.android.quakereport.Earthquake(mag, place, time, Url);
                earthquakes.add(earthquake);

            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) throws MalformedURLException {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // if the request was successful (response code 200),
            // the read the input strem and parse the response
            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the book results", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}