package com.todo.simpletodo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Grant on 12/28/17.
 */

public class ClockActivity extends AppCompatActivity {

    private TextView clock;
    private int length;
    private final int conversion = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_layout);
        clock = (TextView) findViewById(R.id.textClock);
        length = getIntent().getIntExtra(InspectTodoActivity.COUNTDOWN, 30);
        clock.setText(length);
    }

    /**
     *
     */
    private void startCountdown() {
        new CountDownTimer(length*conversion, conversion){
            public void onTick(long timeLeft){
                clock.setText(Long.toString(timeLeft/conversion));
            }
            public void onFinish(){
                clock.setText("Done");
            }
        }.start();
    }

}
