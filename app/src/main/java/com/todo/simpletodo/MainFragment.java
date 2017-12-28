//package com.todo.simpletodo;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//
///**
// * Created by Grant on 12/26/17.
// */
//
//public class MainFragment extends Fragment {
//    private ArrayList<String> items;
//    private ArrayAdapter<String> itemsAdapter;
//    private ListView listView;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        listView = (ListView) getActivity().findViewById(R.id.lvItems);
//        items = new ArrayList<>();
//        itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
//        listView.setAdapter(itemsAdapter);
//        setupOnClickListener();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_main, container, false);
//    }
//
//    private void setupOnClickListener(){
//        listView.setOnItemLongClickListener(
//                new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                        items.remove(position);
//                        itemsAdapter.notifyDataSetChanged();
//                        return true;
//                    }
//                }
//        );
//    }
//
//    /**
//     * Adds new task from textbox to list and clears textbox
//     * @param view the EditText View with new task
//     */
//    public void onClickAdd(View view) {
//        EditText et = (EditText) findViewById(R.id.etNewTask);
//        String text = et.getText().toString();
//        itemsAdapter.add(text);
//        et.setText("");
//    }
//
//}
