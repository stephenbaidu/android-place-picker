package com.github.stephenbaidu.placepicker;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by stephen on 3/1/15.
 */
public class PlaceDetailTask extends AsyncTask<String, Void, PlaceDetail> {
    private static final String LOG_TAG = "PlaceDetailTask";

    private PlacePicker.OnDetailFetched onTaskCompleted;
    private String apiKey;

    public PlaceDetailTask(String apiKey) {
        this.apiKey = apiKey;
        this.onTaskCompleted = null;
    }

    public void setOnTaskCompleted(PlacePicker.OnDetailFetched onDetailFetched) {
        this.onTaskCompleted = onDetailFetched;
    }

    @Override
    protected PlaceDetail doInBackground(String... params) {
        PlaceDetail placeInfoDetail = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PlaceApiRequest.PLACES_API_BASE + PlaceApiRequest.TYPE_DETAILS + PlaceApiRequest.OUT_JSON);
            sb.append("?key=" + this.apiKey);
            sb.append("&placeid=" + URLEncoder.encode(params[0], "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return placeInfoDetail;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return placeInfoDetail;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            JSONObject resultJSONObject = jsonObj.getJSONObject("result");
            String description = resultJSONObject.getString("formatted_address");
            JSONObject geometryJSONObject = resultJSONObject.getJSONObject("geometry");
            Double latitude = geometryJSONObject.getJSONObject("location").getDouble("lat");
            Double longitude = geometryJSONObject.getJSONObject("location").getDouble("lng");

            placeInfoDetail = new PlaceDetail(params[0], description, latitude, longitude);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return placeInfoDetail;
    }

    @Override
    protected void onPostExecute(PlaceDetail placeInfoDetail) {
        super.onPostExecute(placeInfoDetail);
        if(onTaskCompleted != null) {
            onTaskCompleted.completed(placeInfoDetail);
        }
    }
}
