package space.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import space.app.BroadcastReceiver.LocationUpdatesBroadcastReceiver;
import space.app.Database.Entity.CafeEntity;
import space.app.Helper.LocationUtils;
import space.app.R;
import space.app.ViewModel.CafeViewModel;
import space.app.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private FusedLocationProviderClient fusedLocationClient;

    private Location lastKnownLocation;
    private Marker currentLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Đặt kiểu bản đồ
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                lastKnownLocation = location;
                                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                if (currentLocationMarker != null) {
                                    currentLocationMarker.remove();
                                }
                                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của tôi"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
                            } else {
                                fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallBack, null);
                            }
                        }
                    });

        }

        Places.initialize(MapsActivity.this, getString(R.string.google_map_api_key));
        PlacesClient placesClient = Places.createClient(this);
        CafeViewModel cafeViewModel = new ViewModelProvider(MapsActivity.this).get(CafeViewModel.class);
        cafeViewModel.getAllCafe().observe(MapsActivity.this, new Observer<List<CafeEntity>>() {
            @Override
            public void onChanged(List<CafeEntity> cafeEntities) {
                if (!cafeEntities.isEmpty()) {
                    for (CafeEntity cafeEntity : cafeEntities) {
                        String googleMapsUrl = cafeEntity.getLink();
                        LocationUtils locationUtils = new LocationUtils(placesClient);
                        try {
                            locationUtils.extractPlaceIdFromUrl(googleMapsUrl).observe(MapsActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    try {
                                        locationUtils.searchPlaceByAddressQuery(s, MapsActivity.this, new LocationUtils.LocationCallbackOfApp() {
                                            @Override
                                            public void onLocationFetched(Place place) {
                                                LatLng cafe = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                                                mMap.addMarker(new MarkerOptions().position(cafe).title(place.getName()).snippet(place.getAddress()));
                                            }

                                            @Override
                                            public void onLocationNoRequest(Location location) {
                                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                                mMap.addMarker(new MarkerOptions().position(latLng).title(cafeEntity.getResName()).snippet(cafeEntity.getAddress()));
                                            }
                                        });
                                    } catch (URISyntaxException e) {
                                        throw new RuntimeException(e);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lastKnownLocation != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    sharedPreferences.getString("LastLatitude", null);
                    sharedPreferences.getString("LastLongitude", null);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("LastLatitude", String.valueOf(lastKnownLocation.getLatitude()));
                    editor.putString("LastLongitude", String.valueOf(lastKnownLocation.getLongitude()));
                    editor.apply();
                }
            }
        }, 60000);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private final LocationCallback locationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            lastKnownLocation = location;
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            currentLocationMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của tôi"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
        }
    };
}
