package space.app.UI.Features;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import space.app.R;

public class seekBarDistance extends AppCompatActivity {

    private TextView textViewProgress;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filterdistance);

        SeekBar seekBarDistance = findViewById(R.id.seekBarDistance);
        textViewProgress = findViewById(R.id.textViewProgressDistance);
        textViewProgress.setText(seekBarDistance.getProgress() + "km");


        // Set một SeekBar change listener
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewProgress.setText(progress + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

}

