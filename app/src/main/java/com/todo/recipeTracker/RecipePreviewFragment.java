package com.todo.recipeTracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Grant on 1/4/18.
 */

/**
 * Fragment displaying a preview of the recipe
 * Displayed information includes:
 *  - Title
 *  - Length of Recipe (Steps / Minutes)
 *  - Ingredients
 *  - Button to start recipe (This will launch the RecipeStepsFragment)
 */
public class RecipePreviewFragment extends Fragment {

    private String titleMessage;
    private ArrayAdapter<String> displayListAdapter;
    private ListView displayListView;
    private ArrayList<String> displayElements;
    private Button startButton;
    private TextView titleView;
    private static final String TITLE_KEYWORD = "TITLE";

    public String FILENAME;
    private static final String FILE_END = "preview.txt";

    public static RecipePreviewFragment newInstance(String title) {
        RecipePreviewFragment newFragment = new RecipePreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEYWORD, title);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayElements = new ArrayList<>();
        Bundle arguments = getArguments();
        titleMessage = arguments.getString(TITLE_KEYWORD);
        FILENAME = titleMessage.concat(FILE_END);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_preview, container, false);
        displayListView = view.findViewById(R.id.recipe_list_description);
        titleView = view.findViewById(R.id.recipe_title_view);
        titleView.setText(titleMessage);
        startButton = view.findViewById(R.id.start_recipe_button);
        displayListAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_checked, displayElements);
        displayListView.setAdapter(displayListAdapter);
        return view;
    }
}
