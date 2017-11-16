package com.riverlet.ringview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AnnularChartView annularChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        annularChartView = findViewById(R.id.annularChartView);
        findViewById(R.id.text_1).setOnClickListener(this);
        findViewById(R.id.text_2).setOnClickListener(this);
        findViewById(R.id.text_3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_1:
                annularChartView.setData(new float[]{100f, 100f, 100f, 100f, 100f});
                break;
            case R.id.text_2:
                annularChartView.setData(new float[]{100f, 200f, 300f, 400f, 500f});
                break;
            case R.id.text_3:
                annularChartView.setData(new float[]{500f, 100f, 300f, 100f, 600f});
                break;
        }
    }
}
