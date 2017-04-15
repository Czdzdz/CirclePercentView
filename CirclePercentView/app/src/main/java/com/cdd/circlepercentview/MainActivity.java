package com.cdd.circlepercentview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Runnable {

    private CirclePercentView cpv;

    private boolean isRunning;

    private Timer mTimer;
    private int curProgress = 0;
    private TimerTask mTimerTask;
    private int i;

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

    /**
     * 开启一个定时执行的进度
     *
     * @param view
     */
    public void startTimeRunning(View view) {
        Toast.makeText(this, String.valueOf(i++), Toast.LENGTH_SHORT).show();

        cpv.setCurPercent(0);

        //根据当前状态执行中断/开启计时器
        if (isRunning == false) {
            startTimer();
            isRunning = true;
        } else {
            stopTimer();
            startTimer();
            isRunning = true;
        }
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }


    @Override
    public void run() {
        if (curProgress > 100) {
            cpv.setCurPercent(100);
            stopTimer();
        } else {
            cpv.setCurPercent(curProgress);
        }
    }

    //开启计时器任务
    public void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    //每个1s增加15%的进度
                    curProgress += 15;
                    runOnUiThread(MainActivity.this);
                }
            };
        }

        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, 1000, 1000);
        }
    }

    //停止计时器任务
    private void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

        cpv.postInvalidate();
    }
}
