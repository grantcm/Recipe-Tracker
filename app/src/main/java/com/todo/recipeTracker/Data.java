package com.todo.recipeTracker;

import android.app.Activity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Grant on 12/30/17.
 */

public class Data {

    private Activity context;

    public Data(){}

    public  Data(Activity context){
        this.context = context;
    }

    public ArrayList<String> readFile(String fileName) {
        File readable = new File(getDir(context), fileName);
        try {
            return new ArrayList<>(FileUtils.readLines(readable));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void writeFile(String fileName, ArrayList<String> toWrite) {
        File writeable = new File(getDir(context), fileName);
        try {
            FileUtils.writeLines(writeable, toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String fileName){
        File toDelete = new File(getDir(context), fileName);
        toDelete.delete();
    }

    private File getDir(Activity context) {
        return context.getFilesDir();
    }
}
