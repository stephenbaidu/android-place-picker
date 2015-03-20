package com.github.stephenbaidu.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stephenbaidu.androidplacepicker.R;
import com.github.stephenbaidu.placepicker.PlaceDetail;
import com.github.stephenbaidu.placepicker.PlacePicker;


public class MainActivity extends ActionBarActivity {

    // Set your server api_key here
    String api_key = "";

    Button button1, button2;
    TextView textPlaceId, textName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        textPlaceId = (TextView) findViewById(R.id.text_place_id);
        textName = (TextView) findViewById(R.id.text_name);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (api_key.length() == 0) {
                    Toast.makeText(MainActivity.this, "No API Key provided", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, PlacePicker.class);
                intent.putExtra(PlacePicker.PARAM_API_KEY, api_key);
                intent.putExtra(PlacePicker.PARAM_EXTRA_QUERY, "&components=country:gh&types=(cities)");

                startActivityForResult(intent, PlacePicker.REQUEST_CODE_PLACE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (api_key.length() == 0) {
                    Toast.makeText(getBaseContext(), "No API Key provided", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, PlacePicker.class);
                intent.putExtra(PlacePicker.PARAM_API_KEY, api_key);

                startActivityForResult(intent, PlacePicker.REQUEST_CODE_PLACE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PlacePicker.REQUEST_CODE_PLACE && resultCode == RESULT_OK) {
            PlaceDetail placeDetail = PlacePicker.fromIntent(data);
            textPlaceId.setText(placeDetail.placeId);
            textName.setText(placeDetail.description);
            Log.v("=====PlacePicker=====", data.getStringExtra(PlacePicker.PARAM_PLACE_ID));
            Log.v("=====PlacePicker=====", data.getStringExtra(PlacePicker.PARAM_PLACE_DESCRIPTION));
            Log.v("=====PlacePicker=====", data.toString());
            Log.v("=====PlacePicker=====", placeDetail.toString());
        }
    }
}
