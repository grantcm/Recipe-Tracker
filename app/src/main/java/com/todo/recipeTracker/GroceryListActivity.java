package com.todo.recipeTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Grant on 1/8/18.
 */

public class GroceryListActivity extends Activity {
    private ArrayList<GroceryItem> groceryList;
    private String name;
    private TextView title;
    private ListView listView;
    private ArrayAdapter<GroceryItem> groceryItemArrayAdapter;

    public final static String TITLE_INTENT_KEYWORD = "grocery_list_title";
    public final static String GROCERY_LIST_INTENT_KEYWORD = "grocery_list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_list_layout);
        title = findViewById(R.id.grocery_list_title);
        listView = findViewById(R.id.grocery_listview);
        groceryItemArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(groceryItemArrayAdapter);
        parseIntent(getIntent());
    }

    private void parseIntent(Intent intent) {
        name = intent.getStringExtra(TITLE_INTENT_KEYWORD);
        title.setText(name);

        ArrayList<String> temporaryItemList;
        temporaryItemList = intent.getStringArrayListExtra(GROCERY_LIST_INTENT_KEYWORD);
        parseAddedGroceryList(temporaryItemList);
    }

    private void parseAddedGroceryList(ArrayList<String> toAdd) {
        //Parse for number from String
    }
}
