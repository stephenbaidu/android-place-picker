package com.github.stephenbaidu.placepicker;

/**
 * Created by stephen on 3/1/15.
 */
public class PlaceApiRequest {
    private static final String LOG_TAG = "PlaceApiRequest";

    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String TYPE_DETAILS = "/details";
    public static final String OUT_JSON = "/json";

    public static void autocomplete(String apiKey, String extraQuery, String input, AutocompleteTask.OnTaskCompleted onTaskCompleted) {
        AutocompleteTask autocompleteTask = new AutocompleteTask(apiKey, extraQuery);
        autocompleteTask.setOnTaskCompleted(onTaskCompleted);
        autocompleteTask.execute(input, apiKey, extraQuery);
    }

    public static void details(String apiKey, String placeId, PlaceDetailTask.OnTaskCompleted onTaskCompleted) {
        PlaceDetailTask placeDetailTask = new PlaceDetailTask(apiKey);
        placeDetailTask.setOnTaskCompleted(onTaskCompleted);
        placeDetailTask.execute(placeId);
    }
}
