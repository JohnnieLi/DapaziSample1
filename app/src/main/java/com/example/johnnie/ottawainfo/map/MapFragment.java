package com.example.johnnie.ottawainfo.map;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.model.DealerModel;
import com.example.johnnie.ottawainfo.slidingactivity.SlidingActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnnie on 2016-08-07.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , LocationListener {

    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_FINE_LOCATION = 0;
    //private LocationRequest mLocationRequest;
    private static final double
            SEATTLE_LAT = 47.60621,
            SEATTLE_LNG = -122.33207;
    private GoogleApiClient mLocationClient;
    private Location currentLocation;
    private Marker marker;
    private ArrayList<Marker> markers = new ArrayList<>();
    private FragmentActivity mActivity;

    private static final String EXTRAS_HIGHLIGHT_ROOM = "EXTRAS_HIGHLIGHT_ROOM";
    private ArrayList<DealerModel> mModels;


    public ArrayList<Marker> getMarkers() {
        return this.markers;
    }


    public interface OnMapFragmentClicked {
        public void GoToMapActivity();
    }


    private static OnMapFragmentClicked sDummyCallbacks = new OnMapFragmentClicked() {
        @Override
        public void GoToMapActivity() {
            Log.d("GOTOMAPACTIVITY", "call in map fragment");
        }
    };


    private OnMapFragmentClicked mCallbacks = sDummyCallbacks;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public static MapFragment newInstance(String highlightedRoomId) {
        MapFragment fragment = new MapFragment();

        Bundle arguments = new Bundle();
        arguments.putString(EXTRAS_HIGHLIGHT_ROOM, highlightedRoomId);
        fragment.setArguments(arguments);

        return fragment;
    }

    public static MapFragment newInstance(Bundle savedState) {
        MapFragment fragment = new MapFragment();
        fragment.setArguments(savedState);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        // loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        setHasOptionsMenu(true);
        getMapAsync(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mapView = super.onCreateView(inflater, container, savedInstanceState);

        return mapView;

    }


    // implement GoogleApiClient.ConnectionCallbacks interface
    @Override
    public void onConnected(Bundle bundle) {

    }

    // implement GoogleApiClient.ConnectionCallbacks interface
    @Override
    public void onConnectionSuspended(int i) {

    }


    // implement LocationListener interface
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getActivity(), "on Location changed", Toast.LENGTH_SHORT).show();
        if (currentLocation == null) {

            currentLocation = location;
            LatLng latLng = new LatLng(
                    currentLocation.getLatitude(), currentLocation.getLongitude()
            );
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(update);
        }
    }

    // implement LocationListener interface
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    // implement LocationListener interface
    @Override
    public void onProviderEnabled(String provider) {

    }

    // implement LocationListener interface
    @Override
    public void onProviderDisabled(String provider) {

    }

    // implement GoogleApiClient.OnConnectionFailedListener interface
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("connection result", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    // implement OnMapReadyCallback interface
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);


        mLocationClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults);
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mLocationClient.connect();

        if (mModels != null) {
            try {
                geoLocate(mModels);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            gotoLocation(SEATTLE_LAT, SEATTLE_LNG, 15);
        }


        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater(null).inflate(R.layout.map_infowindow, null);
                    TextView tvLocality = (TextView) v.findViewById(R.id.tvLocality);
                    TextView tvLat = (TextView) v.findViewById(R.id.tvLat);
                    TextView tvLng = (TextView) v.findViewById(R.id.tvLng);
                    TextView tvSnippet = (TextView) v.findViewById(R.id.tvSnippet);
                    final String[] messages = marker.getTitle().split("_");
                    tvLocality.setText(messages[4]);
                    tvSnippet.setText(marker.getSnippet());
                    return v;
                }
            });


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(getActivity(), SlidingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("message", marker.getTitle());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }


    // ======================= Helper methods ===============================
    private void gotoLocationWhenMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.30);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(update);
    }


    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);

    }


    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), perm)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, requestCode);
            }
        }
    }


    public void markersFresh() {
        if (markers.size() > 0) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
    }


    public void geoLocateBySingleModel(DealerModel model) throws IOException {

        Geocoder gc = new Geocoder(mActivity);

            String searchString = model.getAddress();
            List<Address> list = gc.getFromLocationName(searchString, 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                String locality = address.getLocality();
                //Toast.makeText(getActivity(), "Found" + locality, Toast.LENGTH_SHORT).show();
                double  lat = address.getLatitude();
                double lng = address.getLongitude();
                addMarker(model, lat, lng);
                gotoLocation(lat, lng, 11);
            }

    }




    public void geoLocate(List<DealerModel> models) throws IOException {
        Log.d("mActivityTest", "geo called");
        Geocoder gc = new Geocoder(mActivity);
        double lat = 0;
        double lng = 0;
        for (DealerModel model : models) {
            String searchString = model.getAddress();
            List<Address> list = gc.getFromLocationName(searchString, 1);

            if (list.size() > 0) {
                Address address = list.get(0);
                String locality = address.getLocality();
                //Toast.makeText(getActivity(), "Found" + locality, Toast.LENGTH_SHORT).show();
                lat = address.getLatitude();
                lng = address.getLongitude();
                addMarker(model, lat, lng);
            }
        }
        mModels = (ArrayList<DealerModel>) models;
        if (markers.size() <= 1) {
            gotoLocation(lat, lng, 15);
        } else {
            gotoLocationWhenMarkers();
        }
    }

    private void addMarker(DealerModel model, double lat, double lng) {
        //Title:address,Id,category,name,info,faq
        String title = model.getAddress()
                + "_" + Long.toString(model.getDealId())
                + "_" + model.getCategory()
                + "_" + model.getName()
                + "_" + model.getInformation()
                + "_" + model.getFAQ();
        MarkerOptions options = new MarkerOptions()
                .title(title)
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        String address = model.getAddress();
        if (address.length() > 0) {
            options.snippet(address);
        }
        marker = mMap.addMarker(options);
        markers.add(marker);
    }


    public void setModels(List<DealerModel> models){
        this.mModels = (ArrayList<DealerModel>)models;
    }

// ======================= Helper methods end ===============================


    @Override
    public void onActivityCreated(Bundle savedinstanceState) {
        super.onActivityCreated(savedinstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("mActivityTest", "attach called");
        this.mActivity = (FragmentActivity) activity;
        if (mActivity == null) {
            Log.d("mActivityTest", "mActivity is null in Attach method");
        }
        if (!(activity instanceof OnMapFragmentClicked)) {
            throw new ClassCastException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (OnMapFragmentClicked) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("mActivityTest", "on Detach  called");
        mCallbacks = sDummyCallbacks;

    }


    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
