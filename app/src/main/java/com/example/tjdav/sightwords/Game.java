package com.example.tjdav.sightwords;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


import static com.example.tjdav.sightwords.MainActivity.SETTINGS_FRAGMENT;
import static java.lang.reflect.Array.getInt;

/**
 * Created by tjdav on 1/20/2018.
 */

public class Game extends Fragment {

    private SightWordAdapter mAdapter;
    private Context mContex;
    private ComsInterface comsInterface;
    private SharedPreferences sharedPreferences;

    private static final int CORRECT_ANSWER = 1;
    private static final int INCORRECT_ANSWER = 0;

    private boolean isGameRunning;
    private boolean isFirstWord;
    private boolean hasSpoken;
    private boolean nextWord;
    private String sightWord;
    private String childName;
    private int luckyNumber;
    private int scoreRight;
    private int scoreWrong;

    ImageView imageSpeak;
    Button btnChoice1;
    Button btnChoice2;
    Button btnChoice3;
    TextView textRight;
    TextView textWrong;
    Button btnSettings;
    Button btnGame;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        comsInterface = (ComsInterface) activity;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        childName = sharedPreferences.getString("name_key", "");
        float tempSpeed = sharedPreferences.getInt("speed_key", 10);
        tempSpeed = tempSpeed / 10;
        float tempPitch = sharedPreferences.getInt("pitch_key", 10);
        tempPitch = tempPitch / 10;
        comsInterface.setSpeed(tempSpeed);
        comsInterface.setPitch(tempPitch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sightword_game_layout, container, false);
        mAdapter = new SightWordAdapter(getActivity().getApplicationContext());



        // UI References
        textRight = (TextView) view.findViewById(R.id.textRight);
        textWrong = (TextView) view.findViewById(R.id.textWrong);
        imageSpeak = (ImageView) view.findViewById(R.id.imageSpeak);
        btnChoice1 = (Button) view.findViewById(R.id.btnChoice1);
        btnChoice2 = (Button) view.findViewById(R.id.btnChoice2);
        btnChoice3 = (Button) view.findViewById(R.id.btnChoice3);
        btnSettings = (Button) view.findViewById(R.id.btnSettings);
        btnGame = (Button) view.findViewById(R.id.btnGame);

        //imageSpeak.setVisibility(View.INVISIBLE);

        isGameRunning = false;
        runGame();

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsFragment();
            }
        });

        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isGameRunning == false) {
                    //btnGame.setText("Stop Game");
                    isGameRunning = true;
                    textRight.setText("0");
                    textWrong.setText("0");
                    if (mAdapter.getCount() >= 6) {
                        isFirstWord = true;
                        runGame();
                    } else {
                        comsInterface.speak(getActivity().getResources()
                                .getString(R.string.six_words_min));
                    }
                } else {
                   // btnGame.setText("Start Game");
                    isGameRunning = false;
                    goodByeMessage();
                    runGame();
                }
            }
        });

        imageSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGameRunning) {
                    if (nextWord) {
                        nextWord = false;
                        imageSpeak.setImageResource(R.drawable.ic_volume_up_black_36dp);
                        runGame();
                        comsInterface.speak(sightWord);
                    } else {
                        comsInterface.speak(sightWord);

                    }
                    hasSpoken = true;
                }

            }
        });

        btnChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nextWord) buttonTest(btnChoice1.getText().toString());
            }
        });

        btnChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nextWord) buttonTest(btnChoice2.getText().toString());
            }
        });

        btnChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nextWord) buttonTest(btnChoice3.getText().toString());

            }
        });

        return view;
    }

    void openSettingsFragment(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Password");
        alert.setIcon(R.drawable.ic_vpn_key_black_36dp);

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);

        alert.setView(input);

        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().matches(getActivity()
                        .getResources().getString(R.string.password))){

                    comsInterface.sendFragmentID(SETTINGS_FRAGMENT);
                    dialogInterface.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Password Incorrect",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.create();
        alert.show();
    }

    String randomMessage(int id){

        String message = "next";

        if(id==CORRECT_ANSWER){
            String[] correctAnswers = getActivity().getResources()
                    .getStringArray(R.array.correct_answers);
            Random random = new Random();
            message = correctAnswers[random.nextInt(correctAnswers.length)];

        } else if(id==INCORRECT_ANSWER){
            String[] incorrectAnswers = getActivity().getResources()
                    .getStringArray(R.array.incorrect_answers);
            Random random = new Random();
            message = incorrectAnswers[random.nextInt(incorrectAnswers.length)];
        }

        return message;
    }

    void goodByeMessage() {
        float avgScore;
        int totalQuestions;

        if(scoreRight>0) {
            totalQuestions = scoreRight + scoreWrong;
            avgScore = (float) scoreRight / totalQuestions;
            avgScore = avgScore * 100;
        }
        else avgScore = 0;

        if (avgScore < 50) {
            comsInterface.speak(getActivity().getResources()
                    .getString(R.string.score_0to49) + childName);
        }
        if ((avgScore >= 50) && (avgScore < 75)) {
            comsInterface.speak(getActivity().getResources()
                    .getString(R.string.score_50to74) + childName);
        }
        if ((avgScore >= 75) && (avgScore < 90)) {
            comsInterface.speak(getActivity().getResources()
                    .getString(R.string.score_75to89) + childName);
        }
        if((avgScore >= 90) && (avgScore <= 100)){
            comsInterface.speak(getActivity().getResources()
                    .getString(R.string.score_90to100) + childName);
        }

        comsInterface.speak(getActivity().getResources().getString(R.string.end_game));
        scoreRight = scoreWrong = 0;
    }

    void buttonTest(String text) {
        if(hasSpoken) {
            if (text.matches(sightWord)) {
                scoreRight++;
                textRight.setText("" + scoreRight);
                comsInterface.speak(randomMessage(CORRECT_ANSWER));

            } else {
                scoreWrong++;
                textWrong.setText("" + scoreWrong);
                comsInterface.speak(randomMessage(INCORRECT_ANSWER));
            }
            nextWord = true;
            if (luckyNumber != 0) btnChoice1.setVisibility(View.INVISIBLE);
            if (luckyNumber != 1) btnChoice2.setVisibility(View.INVISIBLE);
            if (luckyNumber != 2) btnChoice3.setVisibility(View.INVISIBLE);

            imageSpeak.setImageResource(R.drawable.ic_forward_white_36dp);

        } else {
            // Please click speaker to hear next word before making a choice
            comsInterface.speak(getActivity().getResources().getString(R.string.click_speaker));
        }

    }

    void runGame(){
        if(isGameRunning) {
            btnGame.setText("Stop Game");
            nextWord = false;
            imageSpeak.setEnabled(true);
            imageSpeak.setImageResource(R.drawable.ic_volume_up_black_36dp);

            btnChoice1.setVisibility(View.VISIBLE);
            btnChoice2.setVisibility(View.VISIBLE);
            btnChoice3.setVisibility(View.VISIBLE);

            Random random = new Random();

            int[] randomArray = new int[3];

            do {
                for (int i = 0; i < 3; i++) {
                    randomArray[i] = random.nextInt(mAdapter.getCount());
                }
            } while((randomArray[0] == randomArray[1]) ||
                    (randomArray[1] == randomArray[2]) ||
                     randomArray[0] == randomArray[2]);


            btnChoice1.setText(mAdapter.getItem(randomArray[0]).getmRealWord());
            btnChoice2.setText(mAdapter.getItem(randomArray[1]).getmRealWord());
            btnChoice3.setText(mAdapter.getItem(randomArray[2]).getmRealWord());

            luckyNumber = random.nextInt(3);
            sightWord = mAdapter.getItem(randomArray[luckyNumber]).getmRealWord();

            if(isFirstWord){
                comsInterface.speak(sightWord);
                hasSpoken = true;
                isFirstWord = false;
            }else {

                hasSpoken = false;
            }

        } else if(!isGameRunning){
            btnGame.setText("Start Game");
            imageSpeak.setEnabled(false);
            btnChoice1.setVisibility(View.INVISIBLE);
            btnChoice2.setVisibility(View.INVISIBLE);
            btnChoice3.setVisibility(View.INVISIBLE);
        }
    }

}
