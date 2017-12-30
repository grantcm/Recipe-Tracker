package com.todo.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Grant on 12/28/17.
 */

public class ClockActivity extends AppCompatActivity {

    private TextView clock;
    private TextView instructions;
    private long length;
    private long remainingTime = 0;
    private final int conversion = 1000;
    private boolean started = false;
    private boolean finished = false;
    private String message;
    private CountDownTimer countDownTimer;
    private Button button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_layout);
        clock = (TextView) findViewById(R.id.textClock);
        instructions = (TextView) findViewById(R.id.instructions);
        button = (Button) findViewById(R.id.startButton);
        Intent intent = getIntent();
        length = intent.getIntExtra(InspectTodoActivity.COUNTDOWN, 0) * conversion;
        message = intent.getStringExtra(InspectTodoActivity.INSTRUCTION);
        instructions.setText(message);
        clock.setText(Long.toString(length/conversion));
    }

    /**
     *
     */
    private void startCountdown(long length) {
        countDownTimer = new CountDownTimer(length, conversion){
            public void onTick(long timeLeft) {
                clock.setText(Long.toString(timeLeft / conversion));
                remainingTime = timeLeft;
            }

            public void onFinish(){
                clock.setText(R.string.clock_done);
                clock.setTextSize(150);
                finished = true;
                button.setText(R.string.return_button);
            }
        };

        countDownTimer.start();
    }

    public void onClickClockButton(View view){
        if(!started && !finished){
            startCountdown(length);
            button.setText(R.string.stop_timer);
            started=true;
        } else if (started && !finished){
            if (countDownTimer != null) {
                length = remainingTime;
                countDownTimer.cancel();
                button.setText(R.string.start_timer);
                started = false;
            }
        } else {
            finish();
        }
    }

}
