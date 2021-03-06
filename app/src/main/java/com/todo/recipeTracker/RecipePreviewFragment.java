package com.todo.recipeTracker;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Button startButton;
    private TextView titleView;
    private ImageView imageView;
    private EditText editPreviewBox;
    private ConstraintLayout normalPreviewContainer;
    private ConstraintLayout editPreviewContainer;
    private Data data;
    private Boolean dataChanged = false;
    private Boolean inEditView = false;
    private static final String TITLE_KEYWORD = "TITLE";
    private static final String FILE_END = ".txt";
    private static final String PREVIEW_MESSAGE = "No items here. Try pressing the title to add ingredients.";
    private static String IMAGE_PATH = "";

    public static final String INGREDIENTS_FILE_END = "ingredients.txt";


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
        data = new Data(getActivity());
        Bundle arguments = getArguments();
        titleMessage = arguments.getString(TITLE_KEYWORD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_preview, container, false);
        setupViewIDs(view);
        readData();
        return view;
    }

    /**
     * Helper method to find views for each element in layout
     * @param view
     */
    private void setupViewIDs(View view){
        normalPreviewContainer = view.findViewById(R.id.recipe_preview_container);
        editPreviewContainer = view.findViewById(R.id.edit_recipe_description_container);
        displayListView = view.findViewById(R.id.recipe_list_description);
        titleView = view.findViewById(R.id.recipe_title_view);
        setupTitleView();
        startButton = view.findViewById(R.id.start_recipe_button);
        editPreviewBox = view.findViewById(R.id.edit_recipe_description_text);
        displayListAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1);
        displayListView.setAdapter(displayListAdapter);
        imageView = view.findViewById(R.id.recipe_image);
        if (IMAGE_PATH.equals("")) {
            //TODO: Add else for image URI
            imageView.setVisibility(View.GONE);
        }
    }

    private void setupTitleView() {
        titleView.setText(titleMessage);
        titleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changeViewToEdit();
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        readData();
        super.onResume();
    }

    @Override
    public void onPause(){
        writeData();
        super.onPause();
    }

    /**
     * @return an ArrayList representation of the ingredient list
     */
    public ArrayList<String> getIngredientList() {
        return parseArrayAdapterItems();
    }

    /**
     *  Writes data to [RecipeTitle]ingredients.txt file
     */
    private void writeData() {
        if (dataChanged) {
            if (displayListAdapter.getCount() == 1 &&
                    displayListAdapter.getItem(0).equals(PREVIEW_MESSAGE)) {
                displayListAdapter.clear();
            } else {
                String FILENAME = titleMessage.concat(INGREDIENTS_FILE_END);
                data.writeFile(FILENAME, parseArrayAdapterItems());
            }
            dataChanged = false;
        }
    }

    private ArrayList<String> parseArrayAdapterItems () {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i <displayListAdapter.getCount(); i++) {
            output.add(displayListAdapter.getItem(i));
        }
        return output;
    }

    /**
     * Reads in recipe ingredients data
     */
    private void readData() {
        String FILENAME = titleMessage.concat(INGREDIENTS_FILE_END);
        ArrayList<String> displayElements = data.readFile(FILENAME);
        displayListAdapter.clear();
        displayListAdapter.addAll(displayElements);
        if (displayListAdapter.isEmpty()) {
            displayListAdapter.add(PREVIEW_MESSAGE);
        }
        dataChanged = false;
    }

    public boolean handleBackPressed() {
        if (inEditView) {
            if (editPreviewBox.hasFocus()) {
                editPreviewContainer.requestFocus();
                parseEditBox();
            } else {
                changeViewToEdit();
            }
            return true;
        }
        return false;
    }

    /**
     * Converts the ingredient list to a string with each element separated by a newline
     */
    private String parseIngredientList () {
        StringBuilder output = new StringBuilder();
        if (displayListAdapter.getItem(0).equals(PREVIEW_MESSAGE)) {
            return "";
        }
        for (int i = 0; i <displayListAdapter.getCount(); i++) {
            output.append(displayListAdapter.getItem(i));
            output.append("\n");
        }
        return output.toString();
    }

    /**
     * Parses the content of the edit box into the display adapater
     * Splits on the newline character
     */
    private void parseEditBox() {
        String listItems = editPreviewBox.getText().toString();
        String[] items = listItems.split("\n");
        displayListAdapter.clear();
        for (String s : items) {
            displayListAdapter.add(s);
        }
        dataChanged = true;
        writeData();
    }

    /**
     * Changes view to edit or normal
     */
    public void changeViewToEdit () {
        if (inEditView) {
            normalPreviewContainer.setVisibility(View.VISIBLE);
            editPreviewContainer.setVisibility(View.GONE);
            parseEditBox();
            inEditView = false;
        } else {
            normalPreviewContainer.setVisibility(View.GONE);
            editPreviewContainer.setVisibility(View.VISIBLE);
            editPreviewBox.setText(parseIngredientList());
            inEditView = true;
        }
    }
}
