package com.example.johnnie.ottawainfo.ottawainfo;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.map.MapFragment;
import com.example.johnnie.ottawainfo.model.DealerModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Johnnie on 2016-08-08.
 */
public class SearchableActivity extends AppCompatActivity implements MapFragment.OnMapFragmentClicked {

    
    private EditText categoryTextView;
    private MapFragment mapFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mapFragment = MapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_search_map, mapFragment).commit();



       categoryTextView = (EditText) findViewById(R.id.catagory_text);
        // Get the intent, verify the action and get the query
        handleIntent(getIntent());


        Button toGoButton = (Button) findViewById(R.id.ToGo_button);
        toGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextView textView = (TextView) findViewById(R.id.ToGo_text);
                    if(textView!=null) {
                        String searchString = textView.getText().toString();

                        mapFragment.markersFresh();
                        DealerModel temModel = new DealerModel();
                        temModel.setAddress(searchString);
                        ArrayList<DealerModel> models = new ArrayList<DealerModel>();
                        models.add(temModel);
                        mapFragment.geoLocate(models);
                        if (getCurrentFocus() != null) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {

        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Toast.makeText(this,query,Toast.LENGTH_SHORT);
            Log.d("searchableActivity", query);
            categoryTextView.setText(query);

        }
    }


    @Override
    public void GoToMapActivity() {

    }
}
