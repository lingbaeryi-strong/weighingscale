package com.weighingscale;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InstrumentView view = findViewById(R.id.instrument_view);

        view.setData(130f, "--", "2020-3-8", true);
    }
}
