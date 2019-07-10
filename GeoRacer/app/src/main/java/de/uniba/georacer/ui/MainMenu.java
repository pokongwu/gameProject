package de.uniba.georacer.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import de.uniba.georacer.R;
import de.uniba.georacer.service.http.route.WaypointExtractor;

public class MainMenu extends AppCompatActivity {

    private int rounds = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNumberPicker();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);


    }

    private void setupNumberPicker() {
        NumberPicker numberPicker = findViewById(R.id.roundPicker);
        if (numberPicker != null) {
            numberPicker.setValue(3);
            numberPicker.setMinValue(3);
            numberPicker.setMaxValue(10);
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    rounds = newVal;
                }
            });
        }
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameMapView.class);
        intent.putExtra("rounds", rounds);
        startActivity(intent);

    }


}
