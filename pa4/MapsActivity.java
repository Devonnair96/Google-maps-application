package com.example.pa4;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* This application is used to create polylines based on a users input. the user will enter the names of multiple
cities and it will create the polyline based on those latlng
*/
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener {

    private GoogleMap mMap;

    private List<MarkerOptions> markerOptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //initialize stuff
        mMap = googleMap;
        markerOptionList = new ArrayList<>();



    }

    /*On clicking the enter button the function will drop a pin on the users entered location. if the user has
    * entered more than one location on the map it will create lines between the points that the user has entered
    */
    public void onClick(View view)
    {
        //initialize stuff
        EditText editText = (EditText) findViewById(R.id.edittext);
        List<Address> addressList = null;
        String search = editText.getText().toString();

        MarkerOptions markerOptions = new MarkerOptions();
        PolylineOptions polylineOptions = new PolylineOptions();

        //checks the edittext field
        if(search.isEmpty())
        {
            Toast.makeText(this,"Enter a city on the map to make a line", Toast.LENGTH_LONG).show();
        }
        else
        {
            //geocoder for searching locations on the map.
            Geocoder geocoder = new Geocoder(this);
            try
            {
                addressList = geocoder.getFromLocationName(search,1);
                if(addressList != null)
                {
                    for(int i=0;i<addressList.size();i++)
                    {
                        Address searchAddress = addressList.get(i);
                        LatLng latLng = new LatLng(searchAddress.getLatitude(),searchAddress.getLongitude());
                        markerOptions.position(latLng);
                        markerOptions.title(search);
                        markerOptionList.add(markerOptions);
                        mMap.addMarker(markerOptions);
                    }
                }
                //gets position of second to last item in markerOptionsList to use as starting point for polyline
                //and gets position of last item in List to serve as end point of polyline.
                if(markerOptionList.size() > 1)
                {

                   Polyline polyline = mMap.addPolyline(new PolylineOptions()
                           .add(markerOptionList.get(markerOptionList.size()-2).getPosition(),
                                   markerOptionList.get(markerOptionList.size()-1).getPosition()));
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }



        }

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}
