package com.example.parkwayz;

import com.example.parkwayz.MapsActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AppBarConfiguration mAppBarConfiguration;
    private GoogleMap mMap;
    private static final String TAG = "PARKDEBUG";
    private FirebaseFirestore mapDB = FirebaseFirestore.getInstance();
    private CollectionReference ownerRef = mapDB.collection("owners");

    FirebaseAuth mAuth;
    FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // init

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();

//       FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_charging,
                R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavHeader();


////         Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    //Creates homeowner locally from token
    private HomeActivity.Owner createNewOwner(String token, GoogleMap gMap){
        HomeActivity.Owner owner = new HomeActivity.Owner("N/A", "N/A", false);
        addOwnerContent(token, gMap, owner);
        return owner;
    }


    //Adds information to homeowner object
    private void addOwnerContent(String token, final GoogleMap gMap, final HomeActivity.Owner owner) {
        ownerRef.document(token).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        GeoPoint gPoint = documentSnapshot.getGeoPoint("Coordinate");
                        double lat = gPoint.getLatitude();
                        double lng = gPoint.getLongitude();
                        LatLng coord = new LatLng(lat, lng);
                        String address = documentSnapshot.getString("Address");
                        String name = documentSnapshot.getString("Name");
                        boolean isAvailable = documentSnapshot.getBoolean("IsAvailable");

                        gMap.addMarker(new MarkerOptions()
                                .position(coord)
                                .title(address));

                        owner.setAddress(address);
                        owner.setName(name);
                        owner.setAvailable(isAvailable);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    //Homeowner class
    private class Owner{
        String address;
        String name;
        boolean isAvailable;

        public Owner(String address, String name, boolean isAvailable){

            this.address = address;
            this.name = name;
            this.isAvailable = isAvailable;
        }

        public void setAddress(String address){ this.address = address; }
        public String getAddress(){ return address; }

        public void setName(String name){ this.name = name; }
        public String getName(){ return name; }

        public void setAvailable(boolean isAvailable){ this.isAvailable = isAvailable; }
        public boolean getAvailable(){ return isAvailable; }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final TextView Loc_Info = findViewById(R.id.LocInfo);

        //Use coordinates from prev activity to set map view
        double lat = 0;
        double lng = 0;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            lat = extras.getDouble("lat");
            lng = extras.getDouble("lng");
        }
        LatLng CurrentLocation = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CurrentLocation,14.0f));

        //Create a list of homeowner objects from Firebase firestore
        final ArrayList<HomeActivity.Owner> ownerList = new ArrayList();
        mapDB.collection("owners")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeActivity.Owner owner = createNewOwner(document.getId(), mMap);
                                ownerList.add(owner);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        //Homeowner selection
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int check;
                String str1;
                String str2;

                for (HomeActivity.Owner o: ownerList) {
                    str1 = o.getAddress();
                    str2 = marker.getTitle();
                    check = str1.compareToIgnoreCase(str2);
                    if(check == 0){
                        Loc_Info.setText(o.getName() + "\n" + o.getAddress());
                        break;
                    }
                }
                return false;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateNavHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_username);
        TextView navUserEmail = headerView.findViewById(R.id.nav_user_email);

        navUserName.setText(currUser.getDisplayName());
        navUserEmail.setText(currUser.getEmail());

    }
}
