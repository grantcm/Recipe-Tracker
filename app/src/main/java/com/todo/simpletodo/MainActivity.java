package com.todo.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Todo> items;
    private TodoArrayAdapter itemsAdapter;
    private ListView listView;

    //TEST DATA
    private final String[] testData = {"One", "Two", "Three", "Four"};

    public final static String LIST_ITEMS = "com.todo.LIST.ITEMS";
    public final static String TITLE = "com.todo.TITLE";
    public final static String PRIORITY = "como.todo.PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsAdapter = new TodoArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setupOnClickListener();
    }

    private void setupOnClickListener(){
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Launch Inspection Activity
                        Todo todo = items.get(position);
                        launchInspectActivity(todo);
                    }
                }
        );
    }

    /**
     * Launches the activity to inspect the item
     * @param item
     */
    public void launchInspectActivity(Todo item) {
        Intent intent = new Intent(this, InspectTodoActivity.class);
        intent.putExtra(LIST_ITEMS, testData);
        intent.putExtra(TITLE, item.getTitle());
        intent.putExtra(PRIORITY, item.getPriority());
        startActivity(intent);
    }

    /**
     * Adds new task from textbox to list and clears textbox
     * @param view the EditText View with new task
     */
    public void onClickAdd(View view) {
        EditText et = (EditText) findViewById(R.id.etNewTask);
        String text = et.getText().toString();
        Todo todo = new Todo(text, new String[] {});
        if (items.contains(todo)) {
            //Duplicate Entry
        } else {
            itemsAdapter.add(todo);
            et.setText("");
        }
    }

}
