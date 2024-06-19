package space.app.Helper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationUtils {

    public static String expandUrl(String shortUrl) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(shortUrl).openConnection();
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.connect();
        String expandedUrl = httpURLConnection.getHeaderField("Location");
        httpURLConnection.disconnect();
        return expandedUrl;
    }

    public static LatLng extractLatLngFromUrl(String url) {
        // Parse URL and extract lat/lng if available
        if (url != null && url.contains("/maps/place/")) {
            int index = url.indexOf("/maps/place/") + 12;
            String sub = url.substring(index);
            String[] parts = sub.split("/");

            for (String part : parts) {
                if (part.contains("@")) {
                    String[] latLngParts = part.split(",");
                    if (latLngParts.length >= 2) {
                        try {
                            double latitude = Double.parseDouble(latLngParts[0].substring(1));
                            double longitude = Double.parseDouble(latLngParts[1]);
                            return new LatLng(latitude, longitude);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    // Define LatLng class if not available in your project
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
}
