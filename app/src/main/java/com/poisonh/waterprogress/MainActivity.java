package com.poisonh.waterprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    private WaterProgressWithText mWater;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWater = (WaterProgressWithText) findViewById(R.id.wpwt_view);
        mWater.setmScore(650);
        mWater.setProgress(60);
    }
}
