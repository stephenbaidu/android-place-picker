package com.github.stephenbaidu.placepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;


public class PlacePicker extends Activity {

    public static final String PARAM_API_KEY = "place_picker_api_key";
    public static final String PARAM_EXTRA_QUERY = "place_picker_extra_query";
    public static final String PARAM_RESULT = "place_picker_result";
    public static final String PARAM_PLACE_ID = "place_picker_place_id";
    public static final String PARAM_PLACE_DESCRIPTION = "place_picker_place_desc";
    public static final String PARAM_LATITUDE = "place_picker_place_latitude";
    public static final String PARAM_LONGITUDE = "place_picker_place_longitude";

    public static final int REQUEST_CODE_PLACE = 2806;
    public static final int REQUEST_CODE_RECENT_SEARCHES = 2807;

    private PlaceHistoryManager placeHistoryManager;

    ImageView backButton;
    SearchView searchView;
    ListView suggestionsListView;
    ListView historyListView;
    PlaceInfoAdapter suggestionsAdapter;
    PlaceInfoAdapter historyAdapter;
    CardView suggestionsCard;
    CardView historyCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        placeHistoryManager = PlaceHistoryManager.getInstance(this);

        backButton = (ImageView) findViewById(R.id.place_picker_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(null);
            }
        });
        searchView = (SearchView) findViewById(R.id.place_picker_search);

        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.place_picker_search_hint));

        suggestionsCard = (CardView) findViewById(R.id.suggestions_card);
        suggestionsCard.setVisibility(View.GONE);
        historyCard = (CardView) findViewById(R.id.history_card);

        suggestionsAdapter = new PlaceInfoAdapter(this, R.drawable.ic_place_marker);
        historyAdapter = new PlaceInfoAdapter(this, R.drawable.ic_place_history);

        suggestionsListView = (ListView) findViewById(R.id.suggestions_list_view);
        suggestionsListView.setAdapter(suggestionsAdapter);
        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceInfo placeInfo = (PlaceInfo) parent.getItemAtPosition(position);
                placeHistoryManager.updateHistory(placeInfo);
                PlaceDetail placeDetail = new PlaceDetail(placeInfo.placeId, placeInfo.getDescription(), 0.0, 0.0);
                sendResult(placeDetail);
            }
        });

        historyListView = (ListView) findViewById(R.id.history_list_view);
        historyAdapter.updateList(placeHistoryManager.getRecentHistory());
        historyListView.setAdapter(historyAdapter);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceInfo placeInfo = (PlaceInfo) parent.getItemAtPosition(position);
                String moreHistoryText = getResources().getString(R.string.more_recent_history_text);
                if(placeInfo.name.equals(moreHistoryText)) {
                    Intent intent = new Intent(getApplicationContext(), RecentSearchesActivity.class);

                    startActivityForResult(intent, PlacePicker.REQUEST_CODE_RECENT_SEARCHES);
                } else {
                    placeHistoryManager.updateHistory(placeInfo);
                    PlaceDetail placeDetail = new PlaceDetail(placeInfo.placeId, placeInfo.getDescription(), 0.0, 0.0);
                    sendResult(placeDetail);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() < 2) return false;
                String apiKey = getIntent().getStringExtra(PARAM_API_KEY);
                String extraQuery = getIntent().getStringExtra(PARAM_EXTRA_QUERY);
                PlaceApiRequest.autocomplete(apiKey, extraQuery, query, new AutocompleteTask.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(List<PlaceInfo> resultList) {
                        if (resultList == null || resultList.isEmpty()) {
                            suggestionsCard.setVisibility(View.GONE);
                        } else {
                            suggestionsAdapter.updateList(resultList);
                            suggestionsCard.setVisibility(View.VISIBLE);
                        }
                    }
                });

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        historyAdapter.updateList(placeHistoryManager.getRecentHistory());
    }

    private void sendResult(PlaceDetail placeDetail) {
        Intent resultIntent = new Intent();

        if(placeDetail == null) {
            finish();
            return;
        }

        resultIntent.putExtra(PARAM_RESULT, placeDetail);
        resultIntent.putExtra(PARAM_PLACE_ID, placeDetail.placeId);
        resultIntent.putExtra(PARAM_PLACE_DESCRIPTION, placeDetail.description);
        resultIntent.putExtra(PARAM_LATITUDE, placeDetail.latitude);
        resultIntent.putExtra(PARAM_LONGITUDE, placeDetail.longitude);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_RECENT_SEARCHES && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    public static PlaceDetail fromIntent(Intent intent) {
        PlaceDetail placeDetail = intent.getExtras().getParcelable(PARAM_RESULT);

        return placeDetail;
    }
}
