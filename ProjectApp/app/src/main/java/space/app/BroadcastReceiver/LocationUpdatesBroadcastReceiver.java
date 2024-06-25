package space.app.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = LocationUpdatesBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = intent.getParcelableExtra("space.app.extra.LOCATION");
        if (location != null) {
            Log.d(TAG, "New location received: " + location.toString());
        }
        else{
            Log.d(TAG, "New location received: " + "lỗi rồi");
        }
    }
}
