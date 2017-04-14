package com.cdd.circlepercentview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CirclePercentView cpv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        cpv = (CirclePercentView) findViewById(R.id.cpv);
        cpv.setOnClickCirclePercentViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float percent = (float) Math.random() * 99 + 1;
                cpv.setCurPercent(percent);
            }
        });
    }


}
