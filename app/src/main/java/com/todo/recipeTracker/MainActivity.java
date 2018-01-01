package com.todo.recipeTracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recipe> items;
    private RecipeArrayAdapter itemsAdapter;
    private ListView listView;
    private Data data;
    private boolean dataChanged = false;

    //TEST DATA
    private final String[] testData = {"One", "Two", "Three", "Four"};
    private final static String FILENAME = "main.txt";

    public final static String LIST_ITEMS = "com.recipe.LIST.ITEMS";
    public final static String TITLE = "com.recipe.TITLE";
    public final static String PRIORITY = "com.recipe.PRIORITY";

    public MainActivity() {
        this.data = new Data(this);
        this.items = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readData();
        listView = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new RecipeArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setupOnClickListener();
    }

    @Override
    protected void onPause(){
        super.onPause();
        writeData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readData();
    }

    /**
     * Converts recipe list to string list then writes to file
     */
    private void writeData() {
        if (dataChanged) {
            ArrayList<String> itemList = new ArrayList<>();
            for(Recipe r: items) {
                itemList.add(r.getTitle());
            }
            data.writeFile(FILENAME, itemList);
            dataChanged = false;
        }
    }

    /**
     * Reads data from file into item list
     */
    private void readData(){
        ArrayList<String> itemList = data.readFile(FILENAME);
        if (itemList.size() != items.size()) {
            items.clear();
            for(String s: itemList) {
                items.add(new Recipe(s));
            }
            dataChanged = false;
        }
    }

    private void setupOnClickListener(){
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        data.deleteFile(items.get(position).getTitle()+".txt");
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        dataChanged = true;
                        writeData();
                        return true;
                    }
                }
        );

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Launch Inspection Activity
                        Recipe recipe = items.get(position);
                        launchInspectActivity(recipe);
                    }
                }
        );
    }

    /**
     * Launches the activity to inspect the item
     * @param item
     */
    public void launchInspectActivity(Recipe item) {
        Intent intent = new Intent(this, InspectRecipeActivity.class);
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
        Recipe recipe = new Recipe(text, new String[] {});
        if (items.contains(recipe)) {
            //Duplicate Entry
        } else {
            itemsAdapter.add(recipe);
            et.setText("");
        }

        dataChanged = true;
    }

}
