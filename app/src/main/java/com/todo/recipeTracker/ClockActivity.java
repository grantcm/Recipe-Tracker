package com.todo.recipeTracker;

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
    private final long conversion = 1000;
    private final long minutesConversion = 60;
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
        length = intent.getLongExtra(RecipeStepsFragment.COUNTDOWN, 0) * conversion;
        message = intent.getStringExtra(RecipeStepsFragment.INSTRUCTION);
        instructions.setText(message);
        clock.setText(convertToMinuteTimer(length));
    }

    private String convertToMinuteTimer(long time) {
        String minutes, seconds;
        if (time < 60) {
            minutes = "";
        } else {
            minutes = Long.toString(time/conversion / minutesConversion);
        }
        seconds = Long.toString(time / conversion % minutesConversion);
        if (seconds.equals("0")) {
            seconds = "00";
        }
        return minutes.concat(":").concat(seconds);
    }

    /**
     * Initiates countdown timer object and sets up logic for finish
     */
    private void startCountdown(long length) {
        countDownTimer = new CountDownTimer(length, conversion){
            public void onTick(long timeLeft) {
                clock.setText(convertToMinuteTimer(timeLeft));
                remainingTime = timeLeft;
            }

            public void onFinish(){
                clock.setText(R.string.clock_done);
                clock.setTextSize(100);
                finished = true;
                button.setText(R.string.return_button);
            }
        };

        countDownTimer.start();
    }

    /**
     * Sets logic for the timer button
     * Alternates between Start, Stop, and Return when finished
     * @param view
     */
    public void onClickClockButton(View view){
        if(!started && !finished){
            startCountdown(length);
            button.setText(R.string.stop_timer);
            started=true;
        } else if (started && !finished){
            length = remainingTime;
            countDownTimer.cancel();
            button.setText(R.string.start_timer);
            started = false;
        } else {
            finish();
        }
    }

}
