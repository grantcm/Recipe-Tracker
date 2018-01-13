package com.todo.recipeTracker;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recipe> items;
    private ArrayList<String> groceryLists;
    private RecipeArrayAdapter itemsAdapter;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView drawerView;
    private ArrayAdapter<String> drawerAdapter;
    private TextView selectedCount;
    private Data data;
    private Toolbar toolbar;
    private RelativeLayout addItemContainer;
    private MenuItem actionMenuItem;
    private boolean dataChanged = false;
    private boolean viewCheckable = false;
    //TODO: use a map to store positions and count i.e. [RECIPE AT POS] x [QUANTITY]
    private Set<Integer> listPositions;

    private final static String RECIPES_NAV = "Recipes";
    private final static String FILENAME = "main.txt";
    private final static String NAVIGATION_DATA = "nav.txt";

    public final static String LIST_ITEMS = "com.recipe.LIST.ITEMS";
    public final static String TITLE = "com.recipe.TITLE";

    public MainActivity() {
        this.data = new Data(this);
        this.items = new ArrayList<>();
        this.listPositions = new HashSet<>();
        this.groceryLists = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readData();
        setupViews();
        readNavigationData();
        setupOnClickListener();
    }

    private void setupViews() {
        listView = findViewById(R.id.lvItems);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        toolbar = findViewById(R.id.main_view_toolbar);
        addItemContainer = findViewById(R.id.add_recipe_container);
        drawerLayout = findViewById(R.id.navigation_drawer);
        drawerView = findViewById(R.id.navigation);
        drawerAdapter = new ArrayAdapter<>(this,
                R.layout.draw_item_layout   );
        drawerView.setAdapter(drawerAdapter);
        setSupportActionBar(toolbar);
        itemsAdapter = new RecipeArrayAdapter(this, android.R.layout.simple_list_item_1
                , items, this);
        listView.setAdapter(itemsAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open,
                R.string.close
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerView.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        selectItem(0);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        drawerView.setItemChecked(position, true);
        if (position != 0) {
            String title = drawerAdapter.getItem(position);
            launchGroceryListActivity(title);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
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
        //TODO: Rewrite
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch(item.getItemId()) {
            case R.id.action_grocery_bag:
                actionMenuItem = item;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
    /**
     * Sets the checkboxes as viewable and updates the action bar to display context specific actions
     */
    private void updateViewToCheckAble () {
        if(viewCheckable) {
            viewCheckable = false;
            addItemContainer.setVisibility(View.VISIBLE);
            itemsAdapter.setCheckable(viewCheckable);
            itemsAdapter.notifyDataSetChanged();
        } else {
            viewCheckable = true;
            listPositions.clear();
            addItemContainer.setVisibility(View.GONE);
            itemsAdapter.setCheckable(viewCheckable);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    //Adds the clicked item to the set of selected positions
    public void addListPosition(int position) {
        listPositions.add(position);
        if (selectedCount == null) {
            selectedCount = findViewById(R.id.selected_count);
            selectedCount.setText(Integer.toString(listPositions.size()));
        } else {
            selectedCount.setText(Integer.toString(listPositions.size()));
        }
    }

    //Removes the clicked item froms et of positions
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
        writeNavigationData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readData();
        readNavigationData();
    }

    /**
     * Converts recipe list to string list then writes to file
     */
    private void writeData() {
        ArrayList<String> itemList = new ArrayList<>();
        for(Recipe r: items) {
            itemList.add(r.toString());
        }
        data.writeFile(FILENAME, itemList);
    }

    private void writeNavigationData() {
        ArrayList<String> navList = new ArrayList<>();
        for (int i = 1; i < drawerAdapter.getCount(); i++) {
            navList.add(drawerAdapter.getItem(i));
        }
        data.writeFile(NAVIGATION_DATA, navList);
    }
    private void readNavigationData() {
        drawerAdapter.clear();
        drawerAdapter.add(RECIPES_NAV);
        ArrayList<String> navList = data.readFile(NAVIGATION_DATA);
        for (String s: navList) {
            drawerAdapter.add(s);
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
        EditText title = actionMenuItem.getActionView().findViewById(R.id.grocery_list_title_edit);
        launchGroceryListActivity(title.getText().toString());
        actionMenuItem.collapseActionView();
    }

    private void launchGroceryListActivity() {
        ArrayList<String> groceryListItems = parsePositionList();
        String groceryListName = "test";
        Intent intent = new Intent(this, GroceryListActivity.class);

        if (drawerAdapter.getPosition(groceryListName) == -1) {
            intent.putExtra(GroceryListActivity.TITLE_INTENT_KEYWORD, "test");
            intent.putExtra(GroceryListActivity.GROCERY_LIST_INTENT_KEYWORD, groceryListItems);

            drawerAdapter.add(groceryListName);
        } else {
            intent.putExtra(GroceryListActivity.TITLE_INTENT_KEYWORD, "test");
            intent.putExtra(GroceryListActivity.GROCERY_LIST_INTENT_ADDITEMS, groceryListItems);
        }
        startActivity(intent);
    }

    private void launchGroceryListActivity(String groceryListName) {
        ArrayList<String> groceryListItems = parsePositionList();
        Intent intent = new Intent(this, GroceryListActivity.class);

        if (drawerAdapter.getPosition(groceryListName) == -1) {
            intent.putExtra(GroceryListActivity.TITLE_INTENT_KEYWORD, "test");
            intent.putExtra(GroceryListActivity.GROCERY_LIST_INTENT_KEYWORD, groceryListItems);

            drawerAdapter.add(groceryListName);
        } else {
            intent.putExtra(GroceryListActivity.TITLE_INTENT_KEYWORD, "test");
            intent.putExtra(GroceryListActivity.GROCERY_LIST_INTENT_ADDITEMS, groceryListItems);
        }
        startActivity(intent);
    }

    private ArrayList<String> parsePositionList() {
        ArrayList<String> recipeItems = new ArrayList<>();
        for (int i : listPositions) {
            String fileToRead = itemsAdapter.getItem(i).getTitle()
                    .concat(RecipePreviewFragment.INGREDIENTS_FILE_END);
            recipeItems.addAll(data.readFile(fileToRead));
        }
        listPositions.clear();
        return recipeItems;
    }
}
