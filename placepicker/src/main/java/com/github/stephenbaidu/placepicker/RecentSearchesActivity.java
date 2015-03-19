package com.github.stephenbaidu.placepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class RecentSearchesActivity extends ActionBarActivity {

    private PlaceHistoryManager placeHistoryManager;
    ListView historyListView;
    PlaceInfoAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_searches);

        placeHistoryManager = PlaceHistoryManager.getInstance(this);

        historyAdapter = new PlaceInfoAdapter(this, R.drawable.ic_place_history);

        historyListView = (ListView) findViewById(R.id.history_list_view);
        historyAdapter.updateList(placeHistoryManager.getHistoryRecords());
        historyListView.setAdapter(historyAdapter);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceInfo placeInfo = (PlaceInfo) parent.getItemAtPosition(position);
                placeHistoryManager.updateInHistory(placeInfo);

                PlaceDetail placeDetail = new PlaceDetail(placeInfo.placeId, placeInfo.getDescription(), 0.0, 0.0);
                sendResult(placeDetail);
            }
        });
    }



    private void sendResult(PlaceDetail placeDetail) {
        Intent resultIntent = new Intent();

        if(placeDetail == null) {
            finish();
            return;
        }

        resultIntent.putExtra(PlacePicker.PARAM_RESULT, placeDetail);
        resultIntent.putExtra(PlacePicker.PARAM_PLACE_ID, placeDetail.placeId);
        resultIntent.putExtra(PlacePicker.PARAM_PLACE_DESCRIPTION, placeDetail.description);
        resultIntent.putExtra(PlacePicker.PARAM_LATITUDE, "5.45");
        resultIntent.putExtra(PlacePicker.PARAM_LONGITUDE, "-2.1");

        setResult(RESULT_OK, resultIntent);

        finish();
    }
}
