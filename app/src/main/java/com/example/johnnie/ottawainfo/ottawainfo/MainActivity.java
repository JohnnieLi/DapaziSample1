package com.example.johnnie.ottawainfo.ottawainfo;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.list.MyListFragment;
import com.example.johnnie.ottawainfo.localdatabase.AutoDealersDbAdapter;
import com.example.johnnie.ottawainfo.map.MapActivity;
import com.example.johnnie.ottawainfo.map.MapFragment;
import com.example.johnnie.ottawainfo.model.DealerModel;
import com.example.johnnie.ottawainfo.utils.DealersPullParser;
import com.example.johnnie.ottawainfo.utils.RecyclerButtonsAdpter;
import com.example.johnnie.ottawainfo.utils.SpinnerAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnMapFragmentClicked,
        MyListFragment.OnListItemClicked {


    private AutoDealersDbAdapter dbHelper;
    private MapFragment mapFragment;
    private MyListFragment listFragment;
    private static final int REQUEST_FINE_LOCATION = 0;
    private boolean isMapFragment = true;
    private String nameBundle = " ";
    private List<DealerModel> mModels;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //implement the map
        mapFragment = new MapFragment();
        listFragment = new MyListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_map, mapFragment).commit();


        // implement search area, including searchView and spinner
        setSearchArea();

        // insert local data
        setLocalData();

        // set buttons
        setRecyclerViewButtons();

    }

    // implement search area, including searchView and spinner
    private void setSearchArea() {
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        if (searchView != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }

        final String[] spinnerTexts = {"Map", "List"};
        Integer[] spinnerImages = {R.drawable.spinner_map_icon, R.drawable.spinner_list_icon};
        Spinner spinner = (Spinner) findViewById(R.id.spinnerView);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_value_layout,
                spinnerTexts, spinnerImages);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //map view
                    case 0:
                        if (!isMapFragment) {
                            mapFragment.setArguments(getIntent().getExtras());
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_map, mapFragment).commit();
                            isMapFragment = true;
                            goToResultsLocationByName(mapFragment.getView(), nameBundle);
                        }
                        break;
                    //list view
                    case 1:
                        if (isMapFragment) {
                            listFragment.setArguments(getIntent().getExtras());
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_map, listFragment).commit();
                            isMapFragment = false;
                            goToResultsLocationByName(listFragment.getView(), nameBundle);
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //initialize database and load data
    public void setLocalData() {
        dbHelper = new AutoDealersDbAdapter(this);
        dbHelper.open();
        List<DealerModel> models = dbHelper.fetchAllDealers();
        if (models.size() == 0) {
            createData();
        }
    }

    private void createData() {
        DealersPullParser parser = new DealersPullParser();
        List<DealerModel> dealers = parser.parseXML(this);
        for (DealerModel dealer : dealers) {
            dbHelper.createDealer(dealer);
        }

    }

    // set buttons
    public void setRecyclerViewButtons() {
        Map<String, Integer> imageButtonsMap = new HashMap<>();
        imageButtonsMap.put("bmw", R.drawable.imagebutton_bmw_selector);
        imageButtonsMap.put("audi", R.drawable.imagebutton_audi_selector);
        imageButtonsMap.put("mercedes", R.drawable.imagebutton_mercedes_selector);
        final String[] keys = imageButtonsMap.keySet().toArray(new String[imageButtonsMap.size()]);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_buttons);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final RecyclerView.Adapter recycleButtonsAd = new RecyclerButtonsAdpter(imageButtonsMap, new RecyclerButtonsAdpter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mapFragment.markersFresh();
                goToResultsLocationByName(view, keys[position]);
                nameBundle = keys[position];
                Toast.makeText(getApplication(), keys[position], Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(recycleButtonsAd);
    }


    public void goToResultsLocationByName(View v, String name) {


        new AsyncTask<String, Void, List<DealerModel>>() {

            @Override
            protected List<DealerModel> doInBackground(String... name) {
                return dbHelper.fetchDealersByName(name[0]);

            }

            @Override
            public void onPostExecute(List<DealerModel> models) {
                //restore list for mapActivity
                mModels = models;
               //Log.d("MAINACTIVITY",models.get(0).getAddress());
                //
                if (isMapFragment) {
                    try {
                        mapFragment.geoLocate(models);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    listFragment.displayListView(models);
                }
            }
        }.execute(name);

    }


    @Override
    public void onStop() {
        super.onStop();
        dbHelper.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("DealerModels",(Serializable)this.mModels);
                startActivity(intent);
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }



    // implement MapFragment.Callbacks
    @Override
    public void GoToMapActivity() {

        Toast.makeText(this, "this is a test", Toast.LENGTH_SHORT).show();
        Log.d("GOTOMAPACTIVITY", "call in main activity");
    }


    // MyListFragment.OnListItemClicked
    @Override
    public void onListItemClicked() {

    }



    @Override
    public void onResume() {
        super.onResume();
        dbHelper.open();
        Log.d("ONRESUME", "dbHelper re-opened");
    }


}
