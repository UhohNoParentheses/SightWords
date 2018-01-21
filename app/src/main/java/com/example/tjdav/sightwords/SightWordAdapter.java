package com.example.tjdav.sightwords;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjdav on 1/19/2018.
 */

public class SightWordAdapter extends BaseAdapter {

    private Context mContext;
    private List<SightWord> sightWordList = new ArrayList<>();

    private SightWordDatabase mDatabase;
    /*
    private static final SightWordAdapter ourInstance = new SightWordAdapter();

    public static SightWordAdapter getInstance() {

        return ourInstance;
    }*/


    SightWordAdapter(Context context) {
        mContext = context;

        mDatabase = new SightWordDatabase(mContext);

        loadDbIntoArrayList();
    }

    private void loadDbIntoArrayList(){
        // Pull all data from database
        Cursor c = mDatabase.selectAll();
        // If cursor contains data
        if(!c.equals(null)) {
            // move cursor to first row
            c.moveToFirst();
            // get number of rows in cursor, and loop that many times
            for (int i = 0; i < c.getCount(); i++) {
                // Create new TTS object on each iteration to store data in each row
                // using overloaded constructor to pass data.
                SightWord temp = new SightWord
                        (c.getInt(1), c.getString(2), c.getString(3), c.getString(4));
                // take data stored in TTS object and add it in to temp array list
                sightWordList.add(temp);
                // move the cursor to the next row of data
                c.moveToNext();
            }
        } else {
            // If the cursor contained no data, make temp array list null
            sightWordList.equals(null);
        }
    }

    void loadArrayListIntoDB(){

        // Deletes old table and recreates it (see dropTable method in DataManager)
        mDatabase.dropTable();

        // Gets array list size and loops that many times
        for(int i=0; i<sightWordList.size(); i++){

            // Inserts array data in to database
            mDatabase.insert(getItem(i).getmID(),
                    getItem(i).getmRealWord(),
                    getItem(i).getmAltWord1(),
                    getItem(i).getmAltWord2());
        }
    }

    void addEntry(String realWord) {

        SightWord temp = new SightWord();

        // stores data in to TTS object
        temp.setmID(sightWordList.size()+1);
        temp.setmRealWord(realWord);

        // Adds entry to array list
        sightWordList.add(temp);

        // Notifies BaseAdapter of the change
        notifyDataSetChanged();

    }

    public void reOrderArray(){
        for(int i=0; i<sightWordList.size(); i++){
            SightWord tempWord = sightWordList.get(i);
            tempWord.setmID(i+1);
            sightWordList.set(i,tempWord);
        }
    }

    public void reSortArray(){
        int arraySize = (sightWordList.size()-1);
        for(int j = 0; j<arraySize; j ++){
            for(int i=0; i<arraySize; i++) {
                if (getItem(i).getmID() > getItem((i + 1)).getmID()) {
                    SightWord tempHi = getItem(i);
                    SightWord tempLow = getItem((i + 1));
                    sightWordList.set(i, tempLow);
                    sightWordList.set(i + 1, tempHi);
                }
            }
        }
    }

    void deleteAll(){
        sightWordList.clear();
        notifyDataSetChanged();
    }

    void deleteEntry(int n){

        // Removes entry from array list
        sightWordList.remove(n);
        reOrderArray();
        // Notifies BaseAdapter of the change
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // Gets the size of the array
        return sightWordList.size();
    }

    @Override
    public SightWord getItem(int whichItem) {
        // Gets item out of array
        return sightWordList.get(whichItem);
    }

    @Override
    public long getItemId(int whichItem) {
        // Gets item id as a long
        return whichItem;
    }

    @Override
    public View getView(int whichItem, View view, ViewGroup viewGroup) {

        // If view is null, skip
        if(view == null) {
            // Inflates listitem
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        // Gets reference to UI objects
        TextView text_level = (TextView) view.findViewById(R.id.text_level);
        TextView text_sight_word = (TextView) view.findViewById(R.id.textWord);


        // Display data
        text_level.setText("" + getItem(whichItem).getmID());
        text_sight_word.setText(getItem(whichItem).getmRealWord());

        return view;
    }



}
