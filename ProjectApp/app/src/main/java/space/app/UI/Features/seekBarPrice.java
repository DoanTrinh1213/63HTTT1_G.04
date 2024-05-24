package space.app.UI.Features;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import space.app.R;

public class seekBarPrice extends AppCompatActivity {

    private TextView textViewProgress;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filterprice);

        SeekBar seekBarPrice = findViewById(R.id.seekBarPrice);
        textViewProgress = findViewById(R.id.textViewProgressPrice);
        textViewProgress.setText(seekBarPrice.getProgress() + "k");



        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewProgress.setText(progress + "k");
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
