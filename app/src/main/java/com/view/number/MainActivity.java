package com.view.number;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.view.NumberLayout;

public class MainActivity extends AppCompatActivity {
    NumberLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.h);
        layout.setVisible(false);
    }
}
