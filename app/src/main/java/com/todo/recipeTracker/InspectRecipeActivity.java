package com.todo.recipeTracker;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import static android.R.attr.fragment;


/**
 * Created by Grant on 12/26/17.
 */

public class InspectRecipeActivity extends FragmentActivity {

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private final String DISPLAYED_FRAGMENT = "DISPLAY";
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        parseIntent();
        setContentView(R.layout.inspect_activity);
        InspectRecipeFragment inspectActivityFragment = InspectRecipeFragment.newInstance(title);
        fragmentTransaction.add(R.id.main_fragment_container, inspectActivityFragment, DISPLAYED_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        title = intent.getStringExtra(MainActivity.TITLE);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    /**
     * Transitions fragment display from recipe list to edit view
     */
    public void onClickEditRecipe(View v) {
        InspectRecipeFragment fragment = (InspectRecipeFragment) fragmentManager
                .findFragmentByTag(DISPLAYED_FRAGMENT);
        fragment.updateMainViewToEdit();
    }

    public void onClickAddNewRow(View v) {
        InspectRecipeFragment fragment = (InspectRecipeFragment) fragmentManager
                .findFragmentByTag(DISPLAYED_FRAGMENT);
        fragment.addNewRow();
    }
}
