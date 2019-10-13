package com.example.parkwayz;


import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Loc_Info.setText("Test");
        //TextView Loc_Info = (TextView) findViewById(R.id.LocInfo);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng CSUF = new LatLng(33.882434, -117.882466);
        mMap.addMarker(new MarkerOptions().position(CSUF).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CSUF));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CSUF,14.0f));
        LatLng Ex1 = new LatLng(33.880104, -117.882862);
        LatLng Ex2 = new LatLng(33.881590, -117.881498);
        LatLng Ex3 = new LatLng(33.887184, -117.889047);
        mMap.addMarker(new MarkerOptions().position(Ex1).title("Parking #1"));
        mMap.addMarker(new MarkerOptions().position(Ex2).title("Parking #2"));
        mMap.addMarker(new MarkerOptions().position(Ex3).title("Parking #3"));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //mMap.clear();

                //mMap.addMarker(new MarkerOptions().position(point).title("Selected Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,14));
                //Loc_Info.setText("test");

            }
        });


    }
}
