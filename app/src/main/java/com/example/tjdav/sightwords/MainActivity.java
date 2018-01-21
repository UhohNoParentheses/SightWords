package com.example.tjdav.sightwords;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,
                                                               ComsInterface {

    private static final int MY_DATA_CHECK_CODE = 0;

    public static final int GAME_FRAGMENT = 1;
    public static final int SETTINGS_FRAGMENT = 2;
    private boolean is_TTS_AVAILABLE;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //switchFragment(GAME_FRAGMENT);


        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

    }

    void switchFragment(int fragID){
        Fragment frag = null;
        String fragmentID = "";

        switch(fragID){
            case GAME_FRAGMENT:
                fragmentID = "Game";
                frag = new Game();
                break;

            case SETTINGS_FRAGMENT:
                fragmentID = "Settings";
                frag = new Settings();
                break;
        }

        //FragmentManager fManager = getFragmentManager();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragmentContainer, frag, fragmentID).commit();
    }

    public void sendFragmentID(int id){
        switchFragment(id);
    }

    public void onInit(int initStatus){
        if(initStatus == TextToSpeech.SUCCESS){
            if(textToSpeech.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE){
                textToSpeech.setLanguage(Locale.US);
                is_TTS_AVAILABLE = true;
            }
        } else if (initStatus == TextToSpeech.ERROR){
            Toast.makeText(this, "Sorry! TTS Engine failed...", Toast.LENGTH_LONG);
            is_TTS_AVAILABLE = false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode){
            // If request code was for TTS
            case MY_DATA_CHECK_CODE:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                    textToSpeech = new TextToSpeech(this, this);
                } else {
                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
                break;
        }

        switchFragment(GAME_FRAGMENT);
    }

    public void speak(String text){
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, "1");
    }

    public void setPitch(float pitch){
        textToSpeech.setPitch(pitch);
    }

    public void setSpeed(float speed){
        textToSpeech.setSpeechRate(speed);
    }
}
