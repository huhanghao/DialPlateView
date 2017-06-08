package com.example.huhanghao.dialpplanttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DialplateView mDialplateView = (DialplateView) findViewById(R.id.health_indicator);
        mDialplateView.setPointDegree(180);
    }
}
