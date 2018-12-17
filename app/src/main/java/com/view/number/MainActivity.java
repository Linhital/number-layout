package com.view.number;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.view.NumberLayout;

public class MainActivity extends AppCompatActivity {
    NumberLayout layout;
    boolean isv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.h);
        layout.setVisible(true);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isv) {
                    layout.setVisible(isv = false);
                } else {
                    layout.setVisible(isv = true);
                }
            }
        });
    }
}
