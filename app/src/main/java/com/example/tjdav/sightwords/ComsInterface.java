package com.example.tjdav.sightwords;

/**
 * Created by tjdav on 1/20/2018.
 */

public interface ComsInterface {

    void sendFragmentID(int id);
    void speak(String text);
    void setPitch(float pitch);
    void setSpeed(float speed);
}
