package com.github.stephenbaidu.placepicker;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephen on 3/1/15.
 */
public class AutocompleteTask extends AsyncTask<String, Void, List<PlaceInfo>> {
    private static final String LOG_TAG = "AutocompleteTask";

    private OnTaskCompleted onTaskCompleted;
    String apiKey, extraQuery;

    public AutocompleteTask(String apiKey, String extraQuery) {
        this.apiKey = apiKey;
        this.extraQuery = (extraQuery == null)? "" : extraQuery;
        this.onTaskCompleted = null;
    }

    public interface OnTaskCompleted {
        public void onTaskCompleted(List<PlaceInfo> resultList);
    }

    public void setOnTaskCompleted(OnTaskCompleted onTaskCompleted) {
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected List<PlaceInfo> doInBackground(String... params) {
        ArrayList<PlaceInfo> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PlaceApiRequest.PLACES_API_BASE + PlaceApiRequest.TYPE_AUTOCOMPLETE + PlaceApiRequest.OUT_JSON);
            sb.append("?key=" + apiKey);
            sb.append(extraQuery);
            sb.append("&input=" + URLEncoder.encode(params[0], "utf8"));
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
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<PlaceInfo>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                JSONObject place = predsJsonArray.getJSONObject(i);
                String id = place.getString("place_id");
                String name = place.getString("description").split(", ", 2)[0];
                String description = place.getString("description").split(", ", 2)[1];

                resultList.add(new PlaceInfo(id, name, description));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    protected void onPostExecute(List<PlaceInfo> resultList) {
        super.onPostExecute(resultList);
        if(onTaskCompleted != null) {
            onTaskCompleted.onTaskCompleted(resultList);
        }
    }
}
