package com.example.tjdav.sightwords;

/**
 * Created by tjdav on 1/19/2018.
 */

public class SightWord {

        private int mID;
        private String mRealWord;
        private String mAltWord1;
        private String mAltWord2;

        SightWord(){

        }

        SightWord(int id, String realWord, String altWord1, String altWord2){
            mID = id;
            mRealWord = realWord;
            mAltWord1 = altWord1;
            mAltWord2 = altWord2;

        }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmRealWord() {
        return mRealWord;
    }

    public void setmRealWord(String mRealWord) {
        this.mRealWord = mRealWord;
    }

    public String getmAltWord1() {
        return mAltWord1;
    }

    public void setmAltWord1(String mAltWord1) {
        this.mAltWord1 = mAltWord1;
    }

    public String getmAltWord2() {
        return mAltWord2;
    }

    public void setmAltWord2(String mAltWord2) {
        this.mAltWord2 = mAltWord2;
    }
}
