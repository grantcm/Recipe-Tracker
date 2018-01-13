package com.todo.recipeTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Grant on 1/8/18.
 */

public class GroceryListActivity extends Activity {
    private String name;
    private TextView title;
    private ListView listView;
    private ArrayAdapter<GroceryItem> groceryItemArrayAdapter;
    private Data data;
    private String FILENAME;

    private final static String FILE_ENDING = "list.txt";

    public final static String TITLE_INTENT_KEYWORD = "grocery_list_title";
    public final static String GROCERY_LIST_INTENT_KEYWORD = "grocery_list";
    public final static String GROCERY_LIST_INTENT_ADDITEMS = "grocery_list_add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_list_layout);
        data = new Data(this);
        title = findViewById(R.id.grocery_list_title);
        listView = findViewById(R.id.grocery_listview);
        groceryItemArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(groceryItemArrayAdapter);
        parseIntent(getIntent());
    }

    @Override
    public void onResume() {
        readData();
        super.onResume();
    }

    @Override
    public void onPause(){
        writeData();
        super.onPause();
    }

    private void writeData() {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i< groceryItemArrayAdapter.getCount(); i++) {
            GroceryItem item = groceryItemArrayAdapter.getItem(i);
            output.add(item.getName().concat("%").concat(Integer.toString(item.getCount())));
        }
        data.writeFile(FILENAME, output);
    }

    private void readData() {
        ArrayList<String> input = data.readFile(FILENAME);
        groceryItemArrayAdapter.clear();
        for (String s : input) {
            String[] args = s.split("%");
            groceryItemArrayAdapter.add(new GroceryItem(args[0], Integer.parseInt(args[1])));
        }
    }

    private void parseIntent(Intent intent) {
        name = intent.getStringExtra(TITLE_INTENT_KEYWORD);
        title.setText(name);
        FILENAME = name.concat(FILE_ENDING);
        if (intent.hasExtra(GROCERY_LIST_INTENT_KEYWORD)) {
            ArrayList<String> temporaryItemList;
            temporaryItemList = intent.getStringArrayListExtra(GROCERY_LIST_INTENT_KEYWORD);
            parseAddedGroceryList(temporaryItemList);
            writeData();
        } else {
            readData();
        }
    }

    private void parseAddedGroceryList(ArrayList<String> toAdd) {
        Pattern number = Pattern.compile("\\d");
        Pattern name = Pattern.compile("\\D+");
        Matcher matcher;
        int count;
        String groceryName;
        //Parse for number from String
        for (String s : toAdd) {
            count = 0;
            groceryName = "";
            matcher = number.matcher(s);
            if (matcher.find()) {
                count = Integer.parseInt(matcher.group());
            }
            matcher = name.matcher(s);
            if (matcher.find()) {
                groceryName = matcher.group();
            }
            groceryItemArrayAdapter.add(new GroceryItem(groceryName, count));
        }
    }
}
