package com.todo.simpletodo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Grant on 12/26/17.
 */

public class InspectTodoActivity extends AppCompatActivity {

    private TextView title;

    private ListView tasks;
    private ArrayList<String> taskList;
    private InspectArrayAdapter taskAdapter;
    private ProgressBar progress;
    private Handler mHandler;
    private static Logger logger;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_activity);
        title = (TextView) findViewById(R.id.title);
        tasks = (ListView) findViewById(R.id.steps);
        progress = (ProgressBar) findViewById(R.id.progress);
        tasks.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mHandler = new Handler();
        logger = Logger.getLogger(InspectTodoActivity.class.toString());

        Intent intent = getIntent();
        parseIntent(intent);
        progress = new ProgressBar(this);
        progress.setIndeterminate(false);
        progress.setMax(taskList.size());
        taskAdapter = new InspectArrayAdapter(this, android.R.layout.simple_list_item_checked, taskList);
        tasks.setAdapter(taskAdapter);
        setupOnClickListener();
    }

    /**
     * Parses the passed intent for title and list of items
     * @param intent
     */
    private void parseIntent(Intent intent) {
        String pageTitle = intent.getStringExtra(MainActivity.TITLE);
        String[] tasks = intent.getStringArrayExtra(MainActivity.LIST_ITEMS);
        //TODO: Parse enum extra
        //Type priority = intent.get

        title.setText(pageTitle);
        taskList = new ArrayList<>();
        for(String s: tasks){
            taskList.add(s);
        }
    }

    protected void setupOnClickListener(){
        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int count = tasks.getCheckedItemCount();
                        progress.setProgress(count);
                    }
                });
            }
        });
    }
}
