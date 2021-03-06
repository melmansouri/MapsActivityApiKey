package com.mel.mapsactivityapikey;

import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapActivity";

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
        mMap = googleMap;

        //Click sobre el mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("New Marker")
                .draggable(true));

                //Movemos la camara a la nueva posicion
                //moveCamera.Con este metodo se mueve de forma brusca la camara
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        //Eventos markers
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.hideInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng posicion=marker.getPosition();
                marker.setSnippet(posicion.latitude+", "+posicion.longitude);
                marker.showInfoWindow();
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .snippet("Ahora mismo no estamos aqui")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        //zindex  indica que marcador queremos que sobresalga sobre los demas, si hay mas

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Aplicando estilo al mapa
        //El estilo lo podemos generar atravez del generador de estilos https://mapstyle.withgoogle.com/
        //Solo funciona sobre un mapa de tipo normal o hibrido
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
