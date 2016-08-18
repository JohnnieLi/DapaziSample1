package com.example.johnnie.dapazisample1.dapazi;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.johnnie.dapazisample1.R;
import com.example.johnnie.dapazisample1.list.MyListFragment;
import com.example.johnnie.dapazisample1.localdatabase.AutoDealersDbAdapter;
import com.example.johnnie.dapazisample1.map.MapFragment;

import java.io.IOException;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MapFragment.Callbacks,
         MyListFragment.OnListItemClicked{


    private AutoDealersDbAdapter dbHelper;
    private  MapFragment mapFragment;
    private MyListFragment listFragment;
    private static final int REQUEST_FINE_LOCATION = 0;
    private boolean isMapFragment = true;
    private String nameBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        git changed test
//        SupportMapFragment mapFragment = MapFragment.newInstance();
        mapFragment = MapFragment.newInstance();
        listFragment = new MyListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_map, mapFragment).commit();


        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        if (searchView != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }

        dbHelper = new AutoDealersDbAdapter(this);
        dbHelper.open();
        dbHelper.deleteAllDealers();
        dbHelper.insertSomeDealers();

        ImageButton bmwButton = (ImageButton) findViewById(R.id.bmwButton);
        bmwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.markersFresh();
                goToResultsLocationByName(v, "bmw");
                nameBundle = "bmw";
            }
        });


        ImageButton audiButton = (ImageButton) findViewById(R.id.audiButton);
        audiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.markersFresh();
                goToResultsLocationByName(v, "audi");
                nameBundle = "audi";
            }
        });


        ImageButton mercedesButton = (ImageButton) findViewById(R.id.mercedesButton);
        mercedesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.markersFresh();
                goToResultsLocationByName(v, "mercedes");
                nameBundle = "mercedes";
            }
        });
    }


    public void goToResultsLocationByName(View v, String name){


       new AsyncTask<String,Void,Cursor>(){

           @Override
           protected Cursor doInBackground(String... name) {
               return dbHelper.fetchDealersByName(name[0]);

           }

           @Override
           public void onPostExecute(Cursor cursor){
               if(isMapFragment) {
                   cursor.moveToFirst();
                   Log.d("CURSOR COUNT", Integer.valueOf(cursor.getCount()).toString());
                   // Looper.prepare();
                   while (!cursor.isAfterLast()) {
                       String address = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.KEY_ADDRESS));
                       try {
                           Log.d("ADDRESS", address);
                           mapFragment.geoLocate(address);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       Log.d("CURSOR POSITION", Integer.valueOf(cursor.getPosition()).toString());
                       cursor.moveToNext();
                   }
                   cursor.close();
               }else {
                   listFragment.displayListView(cursor);
               }
           }
       }.execute(name);

    }



    @Override
    public void onStop(){
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


        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_listView:
                listFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_map,listFragment).commit();
                isMapFragment = false;
                goToResultsLocationByName(null,nameBundle);
                break;
            case R.id.action_mapView:
                mapFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_map,mapFragment).commit();
                isMapFragment = true;
                goToResultsLocationByName(mapFragment.getView(),nameBundle);
                break;
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



    @Override
    public void GoTo(String address){
        Toast.makeText(this,"this is a test",Toast.LENGTH_SHORT);
    }

    @Override
    public void onListItemClicked() {

    }
}
