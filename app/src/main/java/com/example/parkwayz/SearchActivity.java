package com.example.parkwayz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final EditText addressSearch = findViewById(R.id.searchText);
        addressSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(addressSearch.getText().toString().equals("Address")){
                    addressSearch.getText().clear();
                }

            }

        });

        final Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView errorInfo = findViewById(R.id.errorText);
                errorInfo.setText("");

                String addressStr = addressSearch.getText().toString();
                if(getAddressFromLocationName(addressStr)){
                    Intent addressCoordinates = new Intent(SearchActivity.this, MapsActivity.class);
                    addressCoordinates.putExtra("lat", latitude);
                    addressCoordinates.putExtra("lng", longitude);

                    startActivity(addressCoordinates);
                }
                else{
                    errorInfo.setText("Please enter a valid address or specific location");
                }
            }
        });
    }

    private boolean getAddressFromLocationName(String addressFull){
        if(Geocoder.isPresent()){
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(addressFull, 1);

                if(addresses.size() > 0) {
                    latitude= addresses.get(0).getLatitude();
                    longitude= addresses.get(0).getLongitude();

                    if(latitude == 0 && longitude == 0){
                        return false;
                    }
                    else{
                        return true;
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
        return false;
    }
}

