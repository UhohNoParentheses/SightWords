package com.example.tjdav.sightwords;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import static com.example.tjdav.sightwords.MainActivity.GAME_FRAGMENT;

/**
 * Created by tjdav on 1/20/2018.
 */

public class Settings extends Fragment {

    public static final String SETTING_PREFS = "Settings";
    public static final String NAME = "name_key";
    public static final String VOICE_PITCH = "pitch_key";
    public static final String VOICE_SPEED = "speed_key";

    private SightWordAdapter adapter;
    private EditText editName;
    private EditText editWord;
    private SeekBar seekPitch;
    private SeekBar seekSpeed;

    private ComsInterface comsInterface;

    SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        comsInterface = (ComsInterface) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        // Starts the Database Adapter
        //databaseAdapter = new DatabaseAdapter(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.settings_layout, container, false);
        adapter = new SightWordAdapter(getActivity().getApplicationContext());

        sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        editWord = (EditText) view.findViewById(R.id.editWord);
        editName = (EditText) view.findViewById(R.id.editName);
        seekPitch = (SeekBar) view.findViewById(R.id.seekPitch);
        seekSpeed = (SeekBar) view.findViewById(R.id.seekSpeed);
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        Button btnExit = (Button) view.findViewById(R.id.btnExit);
        Button btnTest = (Button) view.findViewById(R.id.btnTest);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        Button btnDeleteAll = (Button) view.findViewById(R.id.btnDeleteAll);

        listView.setAdapter(adapter);
        editName.setText(sharedPreferences.getString(NAME, ""));
        seekPitch.setProgress(sharedPreferences.getInt(VOICE_PITCH, 100));
        seekSpeed.setProgress(sharedPreferences.getInt(VOICE_SPEED, 100));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addEntry(editWord.getText().toString());
                editWord.setText("");
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteAll();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int whichItem, long l) {
                adapter.deleteEntry(whichItem);

                return false;
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float tempPitch = seekPitch.getProgress();
                tempPitch = tempPitch/10;
                float tempSpeed = seekSpeed.getProgress();
                tempSpeed = tempSpeed/10;

                comsInterface.setPitch(tempPitch);
                comsInterface.setSpeed(tempSpeed);
                comsInterface.speak(getActivity().getResources().getString(R.string.test_message));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(NAME, editName.getText().toString());
                editor.putInt(VOICE_PITCH, seekPitch.getProgress());
                editor.putInt(VOICE_SPEED, seekSpeed.getProgress());

                editor.apply();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comsInterface.sendFragmentID(GAME_FRAGMENT);

            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        adapter.loadArrayListIntoDB();
    }
}
