package com.todo.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextClock;

/**
 * Created by Grant on 12/28/17.
 */

public class ClockActivity extends AppCompatActivity {

    private TextClock clock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_layout);
        clock = (TextClock) findViewById(R.id.textClock);
    }

}
