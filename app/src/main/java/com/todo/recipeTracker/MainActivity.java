package com.todo.recipeTracker;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recipe> items;
    private RecipeArrayAdapter itemsAdapter;
    private ListView listView;
    private TextView selectedCount;
    private Data data;
    private Toolbar toolbar;
    private RelativeLayout addItemContainer;
    private MenuItem actionMenuItem;
    private boolean dataChanged = false;
    private boolean viewCheckable = false;
    //TODO: use a map to store positions and count i.e. [RECIPE AT POS] x [QUANTITY]
    private Set<Integer> listPositions;

    //TEST DATA
    private final String[] testData = {"One", "Two", "Three", "Four"};
    private final static String FILENAME = "main.txt";

    public final static String LIST_ITEMS = "com.recipe.LIST.ITEMS";
    public final static String TITLE = "com.recipe.TITLE";

    public MainActivity() {
        this.data = new Data(this);
        this.items = new ArrayList<>();
        this.listPositions = new HashSet<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readData();
        listView = findViewById(R.id.lvItems);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        toolbar = findViewById(R.id.main_view_toolbar);
        addItemContainer = findViewById(R.id.add_recipe_container);
        setSupportActionBar(toolbar);
        itemsAdapter = new RecipeArrayAdapter(this, android.R.layout.simple_list_item_1
                , items, this);
        listView.setAdapter(itemsAdapter);
        setupOnClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem actionItem = menu.findItem(R.id.action_grocery_bag);
        actionItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                updateViewToCheckAble();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                updateViewToCheckAble();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_grocery_bag:
                actionMenuItem = item;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets the checkboxes as viewable and updates the action bar to display context specific actions
     */
    private void updateViewToCheckAble () {
        if(viewCheckable) {
            viewCheckable = false;
            listPositions.clear();
            addItemContainer.setVisibility(View.VISIBLE);
            itemsAdapter.setCheckable(viewCheckable);
            itemsAdapter.notifyDataSetChanged();
        } else {
            viewCheckable = true;
            addItemContainer.setVisibility(View.GONE);
            itemsAdapter.setCheckable(viewCheckable);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    public void addListPosition(int position) {
        listPositions.add(position);
        if (selectedCount == null) {
            selectedCount = findViewById(R.id.selected_count);
            selectedCount.setText(Integer.toString(listPositions.size()));
        } else {
            selectedCount.setText(Integer.toString(listPositions.size()));
        }
    }

    public void removeListPosition(int position) {
        listPositions.remove(position);
        if (selectedCount == null) {
            selectedCount = findViewById(R.id.selected_count);
            selectedCount.setText(Integer.toString(listPositions.size()));
        } else {
            selectedCount.setText(Integer.toString(listPositions.size()));
        }
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
                itemList.add(r.toString());
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

    /**
     * On long click remove the item from the list, delete its files, and update the view
     * On click launch the inspection activity
     */
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
        startActivity(intent);
    }

    /**
     * Adds new task from textbox to list and clears textbox
     * @param view the EditText View with new task
     */
    public void onClickAdd(View view) {
        EditText et = findViewById(R.id.etNewTask);
        String text = et.getText().toString();
        Recipe recipe = new Recipe(text);
        if (items.contains(recipe)) {
            //Duplicate Entry
        } else {
            itemsAdapter.add(recipe);
            et.setText("");
        }

        dataChanged = true;
    }

    public void onClickAddToGroceryList(View view) {
        launchGroceryListActivity();
        actionMenuItem.collapseActionView();
    }

    private void launchGroceryListActivity() {
        ArrayList<String> groceryListItems = parsePositionList();

        Intent intent = new Intent(this, GroceryListActivity.class);
        intent.putExtra(GroceryListActivity.TITLE_INTENT_KEYWORD, "test");
        intent.putExtra(GroceryListActivity.GROCERY_LIST_INTENT_KEYWORD, groceryListItems);
        startActivity(intent);
    }

    private ArrayList<String> parsePositionList() {
        ArrayList<String> recipeItems = new ArrayList<>();
        for (int i : listPositions) {
            recipeItems.addAll(itemsAdapter.getItem(i).getIngredients());
        }
        return recipeItems;
    }
}
