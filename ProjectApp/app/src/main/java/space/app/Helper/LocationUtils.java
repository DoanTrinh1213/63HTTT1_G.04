package space.app.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.lang.invoke.MutableCallSite;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationUtils {

    private PlacesClient placesClient;
    String fullUrl = null;


    public LocationUtils(PlacesClient placesClient) {
        this.placesClient = placesClient;
    }

    public static class LatLng {
        private double latitude;
        private double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    private void getFullUrl(String shortUrl, final OnUrlRetrievedListener listener) {
        new Thread((new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(shortUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setInstanceFollowRedirects(false);
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                        fullUrl = connection.getHeaderField("Location");
                    } else {
                        fullUrl = shortUrl;
                    }
                    Log.d("fullUrl", fullUrl);
                    try {
                        listener.onUrlRetrieved(fullUrl);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    Log.e("RetrieveURLTask", "Error retrieving URL", e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        })).start();
    }

    public LiveData<String> extractPlaceIdFromUrl(String url) throws URISyntaxException, IOException {
        MutableLiveData<String> data = new MutableLiveData<>();
        getFullUrl(url, new OnUrlRetrievedListener() {
            @Override
            public void onUrlRetrieved(String fullUrl) throws URISyntaxException {
                if (fullUrl != null) {
                    Log.d("url123", fullUrl);
                    URI uri = new URI(fullUrl);
                    String path = uri.getPath();
                    Pattern pattern = Pattern.compile("/place/([^/@]+)");
                    Matcher matcher = pattern.matcher(path);
                    if (matcher.find()) {
                        data.postValue(matcher.group(1));
                    }
                }
            }

            ;
        });
        return data;
    }

    @SuppressLint("MissingPermission")
    public void searchPlaceByAddressQuery(String addressQuery, Context context, LocationCallbackOfApp callback) throws URISyntaxException, IOException {

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(addressQuery)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse response) {
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                if (!predictions.isEmpty()) {
                    AutocompletePrediction topPrediction = predictions.get(0);
                    Log.i("PlaceSearch", "Top place found: " + topPrediction.getPrimaryText(null).toString());
                    fetchPlaceDetails(topPrediction.getPlaceId(), callback);
                } else {
                    Log.i("PlaceSearch", "No place found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("PlaceSearch", "Place autocomplete search failed: " + e.getMessage());
                Log.e("PlaceSearch", "Place autocomplete search failed: " + e.getMessage());
                LatLng latlog = extractLatLngFromUrl(fullUrl);
                Location location = new Location("");
                location.setLatitude(latlog.getLatitude());
                location.setLongitude(latlog.getLongitude());
                Log.d("PlaceDetails", "Latitude longtitude " + latlog.getLatitude() + latlog.getLongitude());
                callback.onLocationNoRequest(location);
            }
        });
    }

    private void fetchPlaceDetails(String placeId, LocationCallbackOfApp callback) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                .build();

        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                Log.i("PlaceDetails", "Place details: " + place.getName() + ", Address: " + place.getAddress() + ", LatLng: " + place.getLatLng());
//                Location location = new Location("");
//                location.setLatitude(place.getLatLng().latitude);
//                location.setLongitude(place.getLatLng().longitude);
                callback.onLocationFetched(place);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("PlaceDetails", "Place details fetch failed: " + e.getMessage());
                LatLng latlog = extractLatLngFromUrl(fullUrl);
                Location location = new Location("");
                location.setLatitude(latlog.getLatitude());
                location.setLongitude(latlog.getLongitude());
                Log.d("PlaceDetails", "Latitude longtitude " + latlog.getLatitude() + latlog.getLongitude());
                callback.onLocationNoRequest(location);
            }
        });
    }

    private interface OnUrlRetrievedListener {
        void onUrlRetrieved(String fullUrl) throws URISyntaxException;
    }

    public interface LocationCallbackOfApp {
        void onLocationFetched(Place place);

        default void onLocationNoRequest(Location location) {
        };
    }

    public Float getDistance(Location location1, Location location2) {
        return location1.distanceTo(location2);
    }

    public static LatLng extractLatLngFromUrl(String url) {
        Pattern pattern = Pattern.compile("/@([-0-9.]+),([-0-9.]+)");
        Matcher matcher = pattern.matcher(url);

        double latitude = 0.0;
        double longitude = 0.0;

        if (matcher.find()) {
            latitude = Double.parseDouble(matcher.group(1));
            longitude = Double.parseDouble(matcher.group(2));
        }

        return new LatLng(latitude, longitude);
    }
}
