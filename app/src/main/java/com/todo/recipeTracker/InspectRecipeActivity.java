package com.todo.recipeTracker;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;


/**
 * Created by Grant on 12/26/17.
 */

public class InspectRecipeActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private final String INSPECT_RECIPE_FRAGMENT = "INSPECT";
    private final String RECIPE_PREVIEW_FRAGMENT = "PREVIEW";
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        parseIntent();
        setContentView(R.layout.inspect_activity);
        RecipePreviewFragment recipePreviewFragment = RecipePreviewFragment.newInstance(title);
        fragmentTransaction.add(R.id.main_fragment_container, recipePreviewFragment, RECIPE_PREVIEW_FRAGMENT);
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

    @Override
    public void onBackPressed() {
        RecipeStepsFragment fragment = (RecipeStepsFragment) fragmentManager
                .findFragmentByTag(INSPECT_RECIPE_FRAGMENT);
        if (fragment != null) {
            if (!fragment.handleBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void onClickAddNewRow(View v) {
        RecipeStepsFragment fragment = (RecipeStepsFragment) fragmentManager
                .findFragmentByTag(INSPECT_RECIPE_FRAGMENT);
        fragment.addNewRow();
    }

    public void onClickStartRecipe(View v) {
        RecipeStepsFragment inspectActivityFragment = RecipeStepsFragment.newInstance(title);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, inspectActivityFragment, INSPECT_RECIPE_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
